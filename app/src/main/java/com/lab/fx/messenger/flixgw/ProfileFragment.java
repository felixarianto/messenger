package com.lab.fx.messenger.flixgw;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lab.fx.library.app.AppUICallback;
import com.lab.fx.library.contact.PersonDB;
import com.lab.fx.library.conversation.MessageDB;
import com.lab.fx.library.conversation.MessageHolder;
import com.lab.fx.library.service.MyServices;
import com.lab.fx.library.service.PrefsDB;
import com.lab.fx.library.util.TimeUtil;
import com.lab.fx.library.widget.EditTextInput;
import com.lab.fx.messenger.MainFragment;
import com.lab.fx.messenger.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ProfileFragment extends MainFragment {

    private OnListFragmentInteractionListener mListener;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private ImageView mThumb;
    private EditTextInput edtx_phone;
    private EditTextInput edtx_name;
    private TextView txt_status;
    private TextView txt_update;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.profile_fragment, container, false);
        mThumb     = (ImageView) view.findViewById(R.id.thumb);
        txt_status = (TextView) view.findViewById(R.id.txt_status);
        txt_update = (TextView) view.findViewById(R.id.txt_update);
        String image_path = PrefsDB.get("my_thumb", null);
        if (image_path != null) {
            Glide.with(getContext())
            .load(image_path)
            .apply(RequestOptions.circleCropTransform().placeholder(R.mipmap.ic_launcher))
            .into(mThumb);
        }
        FloatingActionButton
        button = (FloatingActionButton) view.findViewById(R.id.fab);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        edtx_phone = (EditTextInput) view.findViewById(R.id.txt_phone);
        edtx_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefsDB.put("my_phone", edtx_phone.getText());
            }
        });
        edtx_phone.setText(PrefsDB.get("my_phone", ""));
        edtx_phone.setIcon(R.drawable.ic_chat_bubble_black_24dp);
        edtx_phone.setHint("Phone Number");

        edtx_name = (EditTextInput) view.findViewById(R.id.txt_name);
        edtx_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefsDB.put("my_name", edtx_name.getText());
            }
        });
        edtx_name.setText(PrefsDB.get("my_name", ""));
        edtx_name.setIcon(R.drawable.ic_person_black_24dp);
        edtx_name.setHint("Name");

        ArrayList<MessageHolder> records = MessageDB.getRecords(null,MessageDB.FIELD_CREATED_TIME + " DESC ", null);
        if (!records.isEmpty()) {
            txt_status.setText(records.size() + " messages received");
            txt_update.setText("Last messages at " + TimeUtil.toDateHour(records.get(0).created_time));
        }
        else {
            txt_status.setText("No messages update");
            txt_update.setText("No messages update");
        }
        return view;
    }

    private int REQUEST_IMAGE_CAPTURE     = 1;
    private int REQUEST_IMAGE_PICK        = 3;
    private int REQUEST_CAMERA_PERMISSION = 2;
    private void dispatchTakePictureIntent() {
        boolean granted = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        if (granted) {
                granted = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        Log.d(getClass().getSimpleName(), "Permission " + granted);
        if (!granted) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(new String[]{"From Gallery", "Take Photo"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , REQUEST_IMAGE_PICK);
                }
                else if (which == 1) {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        Log.d(getClass().getSimpleName(), "File Created " + photoFile.getAbsolutePath());
                    } catch (IOException ex) {
                        Log.e(getClass().getSimpleName(), "", ex);
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getActivity(),
                                "com.lab.fx.messenger",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                        Log.d(getClass().getSimpleName(), "Activity Take");
                    }
                }
            }
        });
        builder.create().show();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void incomingData(String p_code, Object p_data) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            String image_path = PrefsDB.get("my_thumb", null);
            if (image_path == null) {
                return;
            }
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(image_path);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            getActivity().sendBroadcast(mediaScanIntent);
            Glide.with(getActivity()).load(image_path)
            .apply(RequestOptions.circleCropTransform().placeholder(R.mipmap.ic_launcher))
            .into(mThumb);
        }
        else if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                File new_file = createImageFile();
                copyFile(new File(getPath(selectedImage)), new_file);
                Glide.with(getActivity()).load(new_file.getAbsolutePath())
                .apply(RequestOptions.circleCropTransform().placeholder(R.mipmap.ic_launcher))
                .into(mThumb);
            } catch (Exception e) {
                Log.e(getClass().getSimpleName(), "", e);
            }
        }
    }

    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor    cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp     = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        PrefsDB.put("my_thumb", image.getAbsolutePath());
        return image;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(String[] item);
    }
}
