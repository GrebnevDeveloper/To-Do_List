package com.developer.grebnev.to_do_list.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.developer.grebnev.to_do_list.MainActivity;
import com.developer.grebnev.to_do_list.R;
import com.developer.grebnev.to_do_list.adapter.TaskAdapter;
import com.developer.grebnev.to_do_list.model.Item;
import com.developer.grebnev.to_do_list.model.ModelTask;

/**
 * Created by Grebnev on 28.11.2015.
 */
public abstract class TaskFragment extends Fragment {
    protected RecyclerView recyclerView;
    protected RecyclerView.LayoutManager layoutManager;

    protected TaskAdapter adapter;

    public MainActivity activity;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity() != null) {
            activity = (MainActivity) getActivity();
        }

        addTaskFromDB();
    }

    public void addTask(ModelTask newTask, boolean savaToDB) {
        int position = -1;

        for (int i = 0; i < adapter.getItemCount(); i++) {
            if (adapter.getItem(i).isTask()) {
                ModelTask task = (ModelTask) adapter.getItem(i);
                if (newTask.getDate() < task.getDate()) {
                    position = i;
                    break;
                }
            }
        }
        if (position != -1) {
            adapter.addItem(position, newTask);
        }
        else {
            adapter.addItem(newTask);
        }

        if (savaToDB == true) {
            activity.dbHelper.saveTask(newTask);
        }
    }

    public void removeTaskDialog(final int location) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

        dialogBuilder.setMessage(R.string.dialog_removing_message);

        Item item = adapter.getItem(location);

        if (item.isTask()) {

            ModelTask removingTask = (ModelTask) item;

            final long timeStamp = removingTask.getTimeStamp();
            final boolean[] isRemoved = {false};

            dialogBuilder.setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    adapter.removeItem(location);
                    isRemoved[0] = true;
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(R.id.coordinator),
                            R.string.removed, Snackbar.LENGTH_LONG);
                    snackbar.setAction(R.string.dialog_cancel, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addTask(activity.dbHelper.query().getTask(timeStamp), false);
                            isRemoved[0] = false;
                        }
                    });
                    snackbar.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                        @Override
                        public void onViewAttachedToWindow(View v) {

                        }

                        @Override
                        public void onViewDetachedFromWindow(View v) {
                            if (isRemoved[0]) {
                                activity.dbHelper.removeTask(timeStamp);
                            }
                        }
                    });
                    snackbar.show();

                    dialog.dismiss();
                }
            });
            dialogBuilder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

        }
        dialogBuilder.show();

    }

    public abstract void addTaskFromDB();

    public abstract void findTasks(String title);

    public abstract void moveTask(ModelTask task);
}
