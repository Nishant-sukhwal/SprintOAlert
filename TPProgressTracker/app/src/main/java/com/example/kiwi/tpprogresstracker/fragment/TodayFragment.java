package com.example.kiwi.tpprogresstracker.fragment;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.kiwi.tpprogresstracker.R;
import com.example.kiwi.tpprogresstracker.adapter.TodayListAdapter;
import com.example.kiwi.tpprogresstracker.classes.ProjectInfo;
import com.example.kiwi.tpprogresstracker.model.SprintInfo;
import com.example.kiwi.tpprogresstracker.ui.ToDOList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends Fragment {

    RecyclerView rvTodaySprint;
    TodayListAdapter todayListAdapter;
    ImageView imgScreenShot, imgToDo, imgEscalate;

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
        todayListAdapter.setOnItemClickListener(new TodayListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (v.getId() == R.id.imgSendMail) {
                    Bitmap bitmap = getRecyclerViewScreenshot(rvTodaySprint, position);
//                imgScreenShot.setImageBitmap(bitmap);
//                imgScreenShot.setVisibility(View.VISIBLE);
                    File mFile = savebitmap(bitmap);
                    Log.d("Item clicked", "onItemClick: " + v.getTag());
                    SprintInfo info = (SprintInfo) v.getTag();
                    final Intent emailIntent1 = new Intent(android.content.Intent.ACTION_SEND);
                    emailIntent1.putExtra(android.content.Intent.EXTRA_SUBJECT, "SprintOAlert | " + info.getProjectName() + " | " + info.getSprintName() + " | status"); //set your subject
                    emailIntent1.putExtra(android.content.Intent.EXTRA_TEXT, "Here is the current status of the sprint."); //set your message
                    if (mFile != null) {
                        emailIntent1.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + mFile.getAbsolutePath()));
                    }
                    emailIntent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    emailIntent1.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    emailIntent1.setType("image/*");
                    startActivityForResult(Intent.createChooser(emailIntent1, "Send Image"), 100);
                } else if (v.getId() == R.id.imgToDo) {
                    startActivity(new Intent(getActivity(), ToDOList.class));

                } else if (v.getId() == R.id.imgEscalate) {

                }
            }
        });
        return view;
    }

    private void initViews(View view) {
        rvTodaySprint = (RecyclerView) view.findViewById(R.id.rvTodaySprints);
        imgScreenShot = (ImageView) view.findViewById(R.id.imgScreenShot);
        imgToDo = (ImageView) view.findViewById(R.id.imgToDo);
        imgEscalate = (ImageView) view.findViewById(R.id.imgEscalate);
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
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        OutputStream outStream = null;
        File file = new File(extStorageDirectory, "snapshot.png");
        if (file.exists()) {
            file.delete();
            file = new File(extStorageDirectory, "snapshot.png");
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
}
