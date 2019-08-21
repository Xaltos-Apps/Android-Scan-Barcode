package com.tiromansev.scanbarcode.vision.camera;

import android.app.Application;
import android.content.Context;

import androidx.annotation.MainThread;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;

import java.util.HashSet;
import java.util.Set;

/** View model for handling application workflow based on camera preview. */
public class WorkflowModel extends AndroidViewModel {

  /**
   * State set of the application workflow.
   */
  public enum WorkflowState {
    NOT_STARTED,
    DETECTING,
    DETECTED,
    CONFIRMING,
    CONFIRMED,
    SEARCHING,
    SEARCHED
  }

  public final MutableLiveData<WorkflowState> workflowState = new MutableLiveData<>();

  public final MutableLiveData<FirebaseVisionBarcode> detectedBarcode = new MutableLiveData<>();

  private final Set<Integer> objectIdsToSearch = new HashSet<>();

  private boolean isCameraLive = false;

  public WorkflowModel(Application application) {
    super(application);
  }

  @MainThread
  public void setWorkflowState(WorkflowState workflowState) {
    this.workflowState.setValue(workflowState);
  }

  public void markCameraLive() {
    isCameraLive = true;
    objectIdsToSearch.clear();
  }

  public void markCameraFrozen() {
    isCameraLive = false;
  }

  public boolean isCameraLive() {
    return isCameraLive;
  }

  private Context getContext() {
    return getApplication().getApplicationContext();
  }
}
