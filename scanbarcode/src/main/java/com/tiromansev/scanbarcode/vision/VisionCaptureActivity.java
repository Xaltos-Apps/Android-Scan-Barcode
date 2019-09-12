package com.tiromansev.scanbarcode.vision;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.internal.Objects;
import com.tiromansev.scanbarcode.PreferenceActivity;
import com.tiromansev.scanbarcode.R;
import com.tiromansev.scanbarcode.vision.camera.CameraSource;
import com.tiromansev.scanbarcode.vision.camera.CameraSourcePreview;
import com.tiromansev.scanbarcode.vision.camera.GraphicOverlay;
import com.tiromansev.scanbarcode.vision.camera.WorkflowModel;
import com.tiromansev.scanbarcode.zxing.BeepManager;

import java.io.IOException;

public class VisionCaptureActivity extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "LiveBarcodeActivity";

    private CameraSource cameraSource;
    private CameraSourcePreview preview;
    private GraphicOverlay graphicOverlay;
    private View settingsButton;
    private View flashButton;
    private TextView promptChip;
    public BeepManager beepManager;
    private AnimatorSet promptChipAnimator;
    private WorkflowModel workflowModel;
    private WorkflowModel.WorkflowState currentWorkflowState;
    private static final int PREFS_REQUEST = 99;
    private VisionActivityHandler handler;
    private boolean started = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vision_capture);
        beepManager = new BeepManager(this);
        preview = findViewById(R.id.camera_preview);
        graphicOverlay = findViewById(R.id.camera_preview_graphic_overlay);
        graphicOverlay.setOnClickListener(this);
        cameraSource = new CameraSource(graphicOverlay);
        handler = new VisionActivityHandler(this);

        promptChip = findViewById(R.id.bottom_prompt_chip);
        promptChipAnimator =
                (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.bottom_prompt_chip_enter);
        promptChipAnimator.setTarget(promptChip);

        findViewById(R.id.close_button).setOnClickListener(this);
        flashButton = findViewById(R.id.flash_button);
        flashButton.setOnClickListener(this);
        settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this);

        setUpWorkflowModel();
    }

    @Override
    protected void onResume() {
        super.onResume();

        workflowModel.markCameraFrozen();
        settingsButton.setEnabled(true);
        currentWorkflowState = WorkflowModel.WorkflowState.NOT_STARTED;
        cameraSource.setFrameProcessor(new BarcodeProcessor(graphicOverlay, workflowModel));
        workflowModel.setWorkflowState(WorkflowModel.WorkflowState.DETECTING);
        beepManager.updatePrefs();
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentWorkflowState = WorkflowModel.WorkflowState.NOT_STARTED;
        stopCameraPreview();
        beepManager.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
            cameraSource = null;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.close_button) {
            onBackPressed();
        } else if (id == R.id.flash_button) {
            setFlash(!flashButton.isSelected());
        } else if (id == R.id.settings_button) {
            Intent intent = new Intent(VisionCaptureActivity.this, PreferenceActivity.class);
            startActivityForResult(intent, PREFS_REQUEST);
        }
    }

    public void setTorch(boolean on) {
        new Thread(() -> {
            while (cameraSource == null || !cameraSource.hasParameters()) {
                try {
                    Thread.sleep(100);
                    // Do some stuff
                } catch (Exception e) {
                    e.getLocalizedMessage();
                }
                Log.d("set_torch", "wait camera...");
            }
            runOnUiThread(() -> setFlash(on));
        }).start();
    }

    private void setFlash(boolean on) {
        if (!on) {
            flashButton.setSelected(false);
            cameraSource.updateFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        } else {
            flashButton.setSelected(true);
            cameraSource.updateFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        }
    }

    private void startCameraPreview() {
        if (!workflowModel.isCameraLive() && cameraSource != null) {
            try {
                workflowModel.markCameraLive();
                preview.start(cameraSource);
            } catch (IOException e) {
                Log.e(TAG, "Failed to start camera preview!", e);
                cameraSource.release();
                cameraSource = null;
            }
        }
    }

    public void playBeepSoundAndVibrate() {
        beepManager.playBeepSoundAndVibrate();
    }

    private void stopCameraPreview() {
        if (workflowModel.isCameraLive()) {
            workflowModel.markCameraFrozen();
            flashButton.setSelected(false);
            preview.stop();
        }
    }

    public void handleDecodeInternally(String rawResult) {
        Log.d("vision_scan", "scan result = " + rawResult);
    }

    private void setUpWorkflowModel() {
        workflowModel = ViewModelProviders.of(this).get(WorkflowModel.class);

        // Observes the workflow state changes, if happens, update the overlay view indicators and
        // camera preview state.
        workflowModel.workflowState.observe(
                this,
                workflowState -> {
                    if (workflowState == null || Objects.equal(currentWorkflowState, workflowState)) {
                        return;
                    }

                    currentWorkflowState = workflowState;
                    Log.d(TAG, "Current workflow state: " + currentWorkflowState.name());

                    boolean wasPromptChipGone = (promptChip.getVisibility() == View.GONE);

                    switch (workflowState) {
                        case DETECTING:
                            promptChip.setVisibility(View.VISIBLE);
                            promptChip.setText(R.string.vision_msg_default_status);
                            startCameraPreview();
                            break;
                        default:
                            promptChip.setVisibility(View.GONE);
                            break;
                    }

                    boolean shouldPlayPromptChipEnteringAnimation =
                            wasPromptChipGone && (promptChip.getVisibility() == View.VISIBLE);
                    if (shouldPlayPromptChipEnteringAnimation && !promptChipAnimator.isRunning()) {
                        promptChipAnimator.start();
                    }
                });

        workflowModel.detectedBarcode.observe(
                this,
                barcode -> {
                    if (started && barcode != null) {
                        started = false;
                        handleDecodeInternally(barcode.getRawValue());
                    }
                });
    }

    public void restartPreviewAfterDelay(long delayMS) {
        Log.d("scan_delay", "stop scanning with delay = " + delayMS);
        handler.sendEmptyMessageDelayed(1, delayMS);
    }

    public void startCapture() {
        Log.d("scan_delay", "refresh after pause");
        started = true;
    }
}
