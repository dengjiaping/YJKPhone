package com.sinosoft.fhcs.android.util;

import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * 褰曢煶
 * Created by fan on 2016/6/23.
 */
public class AudioRecoderUtils {

    //鏂囦欢璺緞
    private String filePath;
    //鏂囦欢澶硅矾寰�
    private String FolderPath;

    private MediaRecorder mMediaRecorder;
    private final String TAG = "fan";
    public static final int MAX_LENGTH = 1000 * 60 * 10;// 鏈�澶у綍闊虫椂闀�1000*60*10;

    private OnAudioStatusUpdateListener audioStatusUpdateListener;

    /**
     * 鏂囦欢瀛樺偍榛樿sdcard/record
     */
    public AudioRecoderUtils(){

        //榛樿淇濆瓨璺緞涓�/sdcard/record/涓�
        this(Environment.getExternalStorageDirectory()+"/record/");
    }

    public AudioRecoderUtils(String filePath) {

        File path = new File(filePath);
        if(!path.exists())
            path.mkdirs();

        this.FolderPath = filePath;
    }

    private long startTime;
    private long endTime;



    /**
     * 寮�濮嬪綍闊� 浣跨敤amr鏍煎紡
     *      褰曢煶鏂囦欢
     * @return
     */
    public void startRecord() {
        // 寮�濮嬪綍闊�
        /* 鈶營nitial锛氬疄渚嬪寲MediaRecorder瀵硅薄 */
        if (mMediaRecorder == null)
            mMediaRecorder = new MediaRecorder();
        try {
            /* 鈶etAudioSource/setVedioSource */
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 璁剧疆楹﹀厠椋�
            /* 鈶¤缃煶棰戞枃浠剁殑缂栫爜锛欰AC/AMR_NB/AMR_MB/Default 澹伴煶鐨勶紙娉㈠舰锛夌殑閲囨牱 */
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            /*
             * 鈶¤缃緭鍑烘枃浠剁殑鏍煎紡锛歍HREE_GPP/MPEG-4/RAW_AMR/Default THREE_GPP(3gp鏍煎紡
             * 锛孒263瑙嗛/ARM闊抽缂栫爜)銆丮PEG-4銆丷AW_AMR(鍙敮鎸侀煶棰戜笖闊抽缂栫爜瑕佹眰涓篈MR_NB)
             */
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            filePath = FolderPath + TimeUtils.getCurrentTime() + ".amr" ;
            /* 鈶㈠噯澶� */
            mMediaRecorder.setOutputFile(filePath);
            mMediaRecorder.setMaxDuration(MAX_LENGTH);
            mMediaRecorder.prepare();
            /* 鈶ｅ紑濮� */
            mMediaRecorder.start();
            // AudioRecord audioRecord.
            /* 鑾峰彇寮�濮嬫椂闂�* */
            startTime = System.currentTimeMillis();
            updateMicStatus();
            Log.e("fan", "startTime" + startTime);
        } catch (IllegalStateException e) {
            Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        } catch (IOException e) {
            Log.i(TAG, "call startAmr(File mRecAudioFile) failed!" + e.getMessage());
        }
    }

    /**
     * 鍋滄褰曢煶
     */
    public long stopRecord() {
        if (mMediaRecorder == null)
            return 0L;
        endTime = System.currentTimeMillis();

        try {

            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

            audioStatusUpdateListener.onStop(filePath);
            filePath = "";

        }catch (RuntimeException e){
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

            File file = new File(filePath);
            if (file.exists())
                file.delete();

            filePath = "";

        }

        return endTime - startTime;
    }

    /**
     * 鍙栨秷褰曢煶
     */
    public void cancelRecord(){

        try {

            mMediaRecorder.stop();
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;

        }catch (RuntimeException e){
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
        }

        File file = new File(filePath);
        if (file.exists())
            file.delete();

        filePath = "";

    }

    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };


    private int BASE = 1;
    private int SPACE = 100;// 闂撮殧鍙栨牱鏃堕棿

    public void setOnAudioStatusUpdateListener(OnAudioStatusUpdateListener audioStatusUpdateListener) {
        this.audioStatusUpdateListener = audioStatusUpdateListener;
    }

    /**
     * 鏇存柊楹﹀厠鐘舵��
     */
    private void updateMicStatus() {

        if (mMediaRecorder != null) {
            double ratio = (double)mMediaRecorder.getMaxAmplitude() / BASE;
            double db = 0;// 鍒嗚礉
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
                if(null != audioStatusUpdateListener) {
                    audioStatusUpdateListener.onUpdate(db,System.currentTimeMillis()-startTime);
                }
            }
            mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
        }
    }

    public interface OnAudioStatusUpdateListener {
        /**
         * 褰曢煶涓�...
         * @param db 褰撳墠澹伴煶鍒嗚礉
         * @param time 褰曢煶鏃堕暱
         */
        public void onUpdate(double db,long time);

        /**
         * 鍋滄褰曢煶
         * @param filePath 淇濆瓨璺緞
         */
        public void onStop(String filePath);
    }

}
