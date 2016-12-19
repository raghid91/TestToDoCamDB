package com.example.acer.testtodocamdb;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    protected ToDoDBHelper toDoDBHelper;
    private List<ToDoItem> todoList;
    private MyAdapter dataAdapter;

    private String imageFile = null;
    ImageButton imageIcon;
    File bufferImage;
    Uri tempUri;

    private static final int CAMERA_REQUEST = 1;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoList = new ArrayList<>();

        toDoDBHelper = new ToDoDBHelper(this);
        imageIcon = (ImageButton) findViewById(R.id.imageButton);


    }

    @Override
    protected void onResume() {
        super.onResume();
        todoList = toDoDBHelper.getAllTasks();
        dataAdapter = new MyAdapter(this, R.layout.todoitem, todoList);
        ListView listTask = (ListView) findViewById(R.id.todoList);
        listTask.setAdapter(dataAdapter);
    }

    public void doAdd(View view) {
        final EditText userInput = new EditText(this);
        userInput.setHint("Enter your Task");
        AlertDialog.Builder adb = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Add Task")
                .setView(userInput)
                .setNegativeButton("Cancel", null)
                .setNeutralButton("Add Picture", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callCamera();
                    }
                })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String input = userInput.getText().toString();
                        if(input.isEmpty()){
                            Toast.makeText(getApplicationContext(),"A ToDo Task Must be entered", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            ToDoItem task = new ToDoItem(input, imageFile, 0);
                            toDoDBHelper.addToDoItem(task);

                            dataAdapter.add(task);
                            dataAdapter.notifyDataSetChanged();
                        }
                    }
                });
        adb.show();
    }

    public void doDelete(View view) {
        toDoDBHelper.deleteToDoItem();
        onResume();
    }

    public void presentImage(View view){

    }

    //******************* ADAPTER ******************************
    private class MyAdapter extends ArrayAdapter<ToDoItem> {
        Context context;
        List<ToDoItem> taskList = new ArrayList<ToDoItem>();

        public MyAdapter(Context c, int rId, List<ToDoItem> objects) {
            super(c, rId, objects);
            taskList = objects;
            context = c;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                CheckBox isDoneChBx = null;
                TextView isDoneTxtBx = null;
                ImageButton isImgBx = null;
                if (convertView == null) {
                    LayoutInflater inflater = (LayoutInflater) context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.todoitem, parent, false);
                    isDoneChBx = (CheckBox) convertView.findViewById(R.id.toDoChkStatus);
                    isDoneTxtBx = (TextView) convertView.findViewById(R.id.toDoChkText);
                    isImgBx = (ImageButton) convertView.findViewById(R.id.imageButton);
                    convertView.setTag(isDoneChBx);
                    isDoneChBx.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CheckBox cb = (CheckBox) view;
                            ToDoItem changeTask = (ToDoItem) cb.getTag();
                            changeTask.setIs_done(cb.isChecked() == true ? 1 : 0);
                            toDoDBHelper.updateTask(changeTask);
                        }
                    });
                } else {
                    isDoneChBx = (CheckBox) convertView.getTag();
                }
                ToDoItem current = taskList.get(position);
                isDoneTxtBx.setText(current.getDescription());
                isImgBx.setTag(imageFile);
                isDoneChBx.setChecked(current.getIs_done() == 1 ? true : false);
                isDoneChBx.setTag(current);

            }catch(Exception e){
                e.printStackTrace();
            }
            return convertView;
        }
    }

    private void callCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        bufferImage = new File(getExternalFilesDir(null),"test.jpg");
        tempUri = Uri.fromFile(bufferImage);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 200);
        intent.putExtra("URI", tempUri);
        intent.putExtra("bufferImage",bufferImage);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==0){
            switch(resultCode){
                case Activity.RESULT_OK:
                    Bundle extras = data.getExtras();
                    if(extras!=null){

                        Toast.makeText(this, "The file was saved at " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                        System.out.println(imageFile);
                    }
                    else{
                        Toast.makeText(this, "No file was saved.", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
                default:
                    break;
            }
        }
    }

}