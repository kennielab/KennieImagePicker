package com.lcw.library.imagepicker.loader;

import android.content.Context;
import android.support.annotation.WorkerThread;

import com.lcw.library.imagepicker.R;
import com.lcw.library.imagepicker.data.MediaFile;
import com.lcw.library.imagepicker.data.MediaFolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 媒体处理类（对扫描出来的图片、视频做对应聚类处理）
 * Create by: chenWei.li
 * Date: 2019/1/22
 * Time: 1:17 AM
 * Email: lichenwei.me@foxmail.com
 */
public class MediaHandler {

    public static final int ALL_MEDIA_FOLDER = -1;//全部媒体
    public static final int ALL_VIDEO_FOLDER = -2;//全部视频


    /**
     *
     * @param context
     * @param mediaFileList
     * @param videoFileList
     * @return
     */
    public static List<MediaFolder> getMediaFolder(Context context, ArrayList<MediaFile> mediaFileList, ArrayList<MediaFile> videoFileList) {

        //根据媒体所在文件夹Id进行聚类（相册）
        Map<Integer, MediaFolder> mediaFolderMap = new HashMap<>();

        //对媒体数据进行排序
        Collections.sort(mediaFileList, new Comparator<MediaFile>() {
            @Override
            public int compare(MediaFile o1, MediaFile o2) {
                if (o1.getDateToken() > o2.getDateToken()) {
                    return -1;
                } else if (o1.getDateToken() < o2.getDateToken()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });

        //全部图片、视频
        MediaFolder allMediaFolder = new MediaFolder(ALL_MEDIA_FOLDER, "所有图片", mediaFileList.get(0).getPath(), mediaFileList);
        mediaFolderMap.put(ALL_MEDIA_FOLDER, allMediaFolder);

        //全部视频
        MediaFolder allVideoFolder = new MediaFolder(ALL_VIDEO_FOLDER, "所有视频", videoFileList.get(0).getPath(), videoFileList);
        mediaFolderMap.put(ALL_VIDEO_FOLDER, allVideoFolder);

        int size = mediaFileList.size();

        //添加其他的图片文件夹
        for (int i = 0; i < size; i++) {
            MediaFile mediaFile = mediaFileList.get(i);
            int imageFolderId = mediaFile.getFolderId();
            MediaFolder mediaFolder = mediaFolderMap.get(imageFolderId);
            if (mediaFolder == null) {
                mediaFolder = new MediaFolder(imageFolderId, mediaFile.getFolderName(), mediaFile.getPath(), new ArrayList<MediaFile>());
            }
            ArrayList<MediaFile> imageList = mediaFolder.getMediaFileList();
            imageList.add(mediaFile);
            mediaFolder.setMediaFileList(imageList);
            mediaFolderMap.put(imageFolderId, mediaFolder);
        }

        //整理聚类数据
        List<MediaFolder> mediaFolderList = new ArrayList<>();
        for (Integer folderId : mediaFolderMap.keySet()) {
            mediaFolderList.add(mediaFolderMap.get(folderId));
        }

        return mediaFolderList;
    }

}
