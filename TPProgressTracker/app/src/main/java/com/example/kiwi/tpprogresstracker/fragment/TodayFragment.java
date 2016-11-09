package com.example.kiwi.tpprogresstracker.fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kiwi.tpprogresstracker.R;
import com.example.kiwi.tpprogresstracker.adapter.TodayListAdapter;
import com.example.kiwi.tpprogresstracker.callback.OnRefreshProject;
import com.example.kiwi.tpprogresstracker.classes.ActionItems;
import com.example.kiwi.tpprogresstracker.classes.ProjectInfo;
import com.example.kiwi.tpprogresstracker.model.HeaderItem;
import com.example.kiwi.tpprogresstracker.model.InnerActionItems;
import com.example.kiwi.tpprogresstracker.model.ListItem;
import com.example.kiwi.tpprogresstracker.model.SprintInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends Fragment {

    RecyclerView rvTodaySprint;
    TodayListAdapter todayListAdapter;
    ImageView imgScreenShot, imgToDo, imgEscalate;
    private ProgressDialog m_ProgressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    View mView;
    int mPosition;
    private OnRefreshProject mCallback;

    public TodayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        initViews(view);
        todayListAdapter = new TodayListAdapter(getContext(), ProjectInfo.getInstance().getProjectList());
        rvTodaySprint.setAdapter(todayListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvTodaySprint.setLayoutManager(linearLayoutManager);
        todayListAdapter.notifyDataSetChanged();
        // Inflate the layout for this fragment
        rvTodaySprint.setClickable(true);
        setOnItemClickListener();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mCallback != null) {
                    mCallback.onRefreshList();
                }
                // Stop refresh animation
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    private void setOnItemClickListener() {
        todayListAdapter.setOnItemClickListener(new TodayListAdapter.MyClickListener() {
            @Override
            public void onItemClick(final int position, View v) {
                if (v.getId() == R.id.imgSendMail) {
//                    imgScreenShot.setImageBitmap(bitmap);
//                    imgScreenShot.setVisibility(View.VISIBLE);
                    mView = v;
                    mPosition = position;
                    checkForPermission(v, position);

//                    shareDataExternally(v, position);
                } else if (v.getId() == R.id.imgToDo) {
//                    SprintInfo info = (SprintInfo) v.getTag();
//                    Intent intent = new Intent(getActivity(), ToDOList.class);
//                    intent.putExtra("day", info.getCurrentDay());
//                    intent.putExtra("project_id", info.getProjectId());
//                    startActivity(intent);
                } else if (v.getId() == R.id.imgEscalate) {
                    final SprintInfo info = (SprintInfo) v.getTag();
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                    builder1.setMessage("Do you want to escalate this sprint?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                                    builder1.setMessage("Will you able to send build today?");
                                    builder1.setCancelable(true);

                                    builder1.setPositiveButton(
                                            "Yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                    Bitmap bitmap = getRecyclerViewScreenshot(rvTodaySprint, position);
                                                    File mFile = savebitmap(bitmap);
                                                    StringBuilder stringBuilder = new StringBuilder();

                                                    ActionItems actionItems = new ActionItems(getActivity(), info.getProjectId(), info.getCurrentDay());
                                                    ArrayList<ListItem> actionListItems = actionItems.getActionItems();
                                                    stringBuilder.append("<b>Action Items :</b>");
                                                    for (ListItem items : actionListItems) {
                                                        if (items.getType() == ListItem.TYPE_HEADER) {
                                                            HeaderItem item = (HeaderItem) items;
                                                            stringBuilder.append("<br/><b>day " + item.getDay() + "</b><br/>");
                                                        } else {
                                                            InnerActionItems innerActionItems = (InnerActionItems) items;
                                                            stringBuilder.append("&#8226; <small>" + innerActionItems.getItem() + "</small><br/>");
                                                        }
                                                    }
                                                    final Intent emailIntent1 = new Intent(Intent.ACTION_SEND);
                                                    emailIntent1.putExtra(Intent.EXTRA_SUBJECT, "SprintOAlert | " + info.getProjectName() + " | " + info.getSprintName() + " | status"); //set your subject
                                                    emailIntent1.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(stringBuilder.toString())); //set your message
                                                    if (mFile != null) {
                                                        emailIntent1.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + mFile.getAbsolutePath()));
                                                    }
                                                    emailIntent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                    emailIntent1.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                                    emailIntent1.setType("image/*");
                                                    startActivityForResult(Intent.createChooser(emailIntent1, "Send Image"), 100);
                                                }
                                            });

                                    builder1.setNegativeButton(
                                            "No",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.cancel();
                                                }
                                            });

                                    AlertDialog alert11 = builder1.create();
                                    alert11.show();
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
            }

        });
    }

    private void shareDataExternally(View v, int position) {
        Bitmap bitmap = getRecyclerViewScreenshot(rvTodaySprint, position);
        File mFile = savebitmap(bitmap);
        Log.d("Item clicked", "onItemClick: " + v.getTag());
        SprintInfo info = (SprintInfo) v.getTag();
        StringBuilder stringBuilder = new StringBuilder();

        ActionItems actionItems = new ActionItems(getActivity(), info.getProjectId(), info.getCurrentDay());
        ArrayList<ListItem> actionListItems = actionItems.getActionItems();
        stringBuilder.append("<b>Action Items :</b>");
        for (ListItem items : actionListItems) {
            if (items.getType() == ListItem.TYPE_HEADER) {
                HeaderItem item = (HeaderItem) items;
                stringBuilder.append("<br/><b>day " + item.getDay() + "</b><br/>");
            } else {
                String actionItem = "No Action taken";
                InnerActionItems innerActionItems = (InnerActionItems) items;
                if (!innerActionItems.getItem().equals("Action to be taken")) {
                    actionItem = innerActionItems.getItem();
                }
                stringBuilder.append("&#8226; <small>" + actionItem + "</small><br/>");
            }
        }
        final Intent emailIntent1 = new Intent(Intent.ACTION_SEND);
        emailIntent1.putExtra(Intent.EXTRA_SUBJECT, "SprintOAlert | " + info.getProjectName() + " | " + info.getSprintName() + " | status"); //set your subject
        //emailIntent1.putExtra(android.content.Intent.EXTRA_TEXT, "Here is the current status of the sprint."); //set your message

        emailIntent1.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(stringBuilder.toString()));
        if (mFile != null) {
            emailIntent1.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + mFile.getAbsolutePath()));
        }
//        emailIntent1.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + getActivity().getFileStreamPath("snapshot.png")));

        emailIntent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        emailIntent1.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        emailIntent1.setType("image/*");
        startActivityForResult(Intent.createChooser(emailIntent1, "Send Image"), 100);
    }

    private void checkForPermission(View v, int position) {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        } else {
            shareDataExternally(v, position);
        }
    }

    private void initViews(View view) {
        rvTodaySprint = (RecyclerView) view.findViewById(R.id.rvTodaySprints);
        imgScreenShot = (ImageView) view.findViewById(R.id.imgScreenShot);
        imgToDo = (ImageView) view.findViewById(R.id.imgToDo);
        imgEscalate = (ImageView) view.findViewById(R.id.imgEscalate);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Toast.makeText(getActivity(), "permission was granted!", Toast.LENGTH_SHORT).show();
                    shareDataExternally(mView, mPosition);

                } else {
                    Toast.makeText(getActivity(), "permission denied!", Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void doWork() {
        if (!isAdded()) return;
        if (todayListAdapter != null) {
            todayListAdapter.notifyDataSetChanged();
        }
    }

    public static Bitmap getRecyclerViewScreenshot(RecyclerView view, int position) {
        RecyclerView.ViewHolder holder = view.getAdapter().createViewHolder(view, 0);
        view.getAdapter().onBindViewHolder(holder, position);
        holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());

        View bitmapView = holder.itemView.findViewById(R.id.layoutSprintData);
        Bitmap bigBitmap = Bitmap.createBitmap(bitmapView.getMeasuredWidth(), bitmapView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas bigCanvas = new Canvas(bigBitmap);
        holder.itemView.draw(bigCanvas);
        return bigBitmap;
    }

    private File savebitmap(Bitmap bmp) {

        String temp = "SplashItShare";
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        String path = Environment.getExternalStorageDirectory()
                .toString();
        new File(path + "/SplashItTemp").mkdirs();
        File file = new File(path + "/SplashItTemp", temp + ".png");
        if (file.exists()) {
            file.delete();
            file = new File(path + "/SplashItTemp", temp + ".png");
        }

        try {
            outStream = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }

    public void setSwipeRefreshListener(OnRefreshProject callback) {
        mCallback = callback;
    }
}
