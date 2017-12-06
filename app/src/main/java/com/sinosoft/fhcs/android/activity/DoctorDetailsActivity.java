package com.sinosoft.fhcs.android.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.base.BaseActivity;
import com.sinosoft.fhcs.android.adapter.ChatMsgViewAdapter;
import com.sinosoft.fhcs.android.customview.CircleImageView;
import com.sinosoft.fhcs.android.customview.RecordButton;
import com.sinosoft.fhcs.android.customview.RecordButton.OnFinishedRecordListener;
import com.sinosoft.fhcs.android.entity.ChatMsgEntity;
import com.sinosoft.fhcs.android.entity.DoctorBean;
import com.sinosoft.fhcs.android.util.CommonUtil;
import com.sinosoft.fhcs.android.util.Constant;
import com.sinosoft.fhcs.android.util.LoadLocalImageUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DoctorDetailsActivity extends BaseActivity implements OnClickListener{

    private Button btnOpenAbstrace;
    private LinearLayout llAbstract;
    private Button btnStartConsult;
    private LinearLayout llConsultBody;
    private RelativeLayout llRelevantContent;
    private boolean isAbstract;//简介默认是展开的 false 位展开
    private Button btnMore;
    private boolean isMore;//选择更多 默认时false false为隐藏
    private ListView lvChat;
    private EditText etConsult;
    private List<ChatMsgEntity> msgList ;//消息列表
    private ChatMsgViewAdapter chatMsgViewAdapter;
    private DoctorDetailsActivity mContext;

    private boolean isSend; //是否为发送状态  true为是
    private Button btnPicture;
    private Button btnPhotograph;
    private Button btnHistory;
    private Button btnDoctorDetailsVoice;
    private File tempFile;
    private boolean isVoice;//为true时，是发送语音
    private RecordButton btnRecord;
    private ImageView ivLine;
    private Button btnBack;
    private DoctorBean doctor;
    private TextView tvDoctorName;
    private int departmentFlage;
    private TextView tvDoctorDepartment;
    private TextView tvJob;
    private TextView tvGood;
    private TextView tvAbstract;
    private TextView tvTitle;
    private ImageView ivDepartment;
    private LoadLocalImageUtil imageLoadInstance;
    private CircleImageView ivHead;

    private static final int PHOTO_REQUEST_CAREMA = 1;
    private static final int PHOTO_REQUEST_GALLERY = 2;
    private static final int PHOTO_REQUEST_CUT = 3;

    private static final int PHOTO_REQUEST_SCREENSHOT = 4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void setUpViewAndData() {
        setContentView(R.layout.activity_doctor_details);
        mContext = this;
        initView();
    }


    private void initView() {
        // TODO Auto-generated method stub

        imageLoadInstance = LoadLocalImageUtil.getInstance();
        btnBack = (Button) findViewById(R.id.titlebar_btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);

        doctor = (DoctorBean)getIntent().getExtras().get("doctor");
        departmentFlage = getIntent().getFlags();
        if(doctor == null){
            return;
        }
        tvTitle = (TextView) findViewById(R.id.titlebar_tv_title);
        tvTitle.setText(doctor.getDoctorName());

        tvDoctorName = (TextView)findViewById(R.id.tv_doctor_details_name);//医师名称
        tvDoctorDepartment = (TextView)findViewById(R.id.tv_doctor_details_department);//科室
        tvJob = (TextView)findViewById(R.id.tv_doctor_details_department_job);//医生职位
        tvGood = (TextView)findViewById(R.id.tv_doctor_detail_good);//医生擅长
        tvAbstract = (TextView)findViewById(R.id.tv_doctor_detail_abstract);//医生简介
        ivDepartment = (ImageView)findViewById(R.id.iv_doctor_details_department);//
        ivHead = (CircleImageView)findViewById(R.id.iv_doctor_details_head);//医生头像

        tvDoctorName.setText(doctor.getDoctorName());
        tvJob.setText(doctor.getDoctorJob());
        tvGood.setText(doctor.getDoctorGood());
        tvAbstract.setText(doctor.getDoctorAbstrack());

        switch (departmentFlage) {
            case Constant.department_erke://儿科
                tvDoctorDepartment.setText("儿科");
                ivDepartment.setBackgroundResource(R.drawable.icon_department_pediatrics);
                break;
            case Constant.department_fuke://妇科
                tvDoctorDepartment.setText("妇科");
                ivDepartment.setBackgroundResource(R.drawable.icon_department_gynecology);
                break;
            case Constant.department_zhongyike://中医科
                tvDoctorDepartment.setText("中医科");
                ivDepartment.setBackgroundResource(R.drawable.icon_department_medicine);
                break;
            case Constant.department_pifuke://皮肤科
                tvDoctorDepartment.setText("皮肤科");
                ivDepartment.setBackgroundResource(R.drawable.icon_department_dermatologist);
                break;
            case Constant.department_xinlike://心理科
                tvDoctorDepartment.setText("心理科");
                ivDepartment.setBackgroundResource(R.drawable.icon_department_psychological_clinic);
                break;
            case Constant.department_xiaohuake://消化科
                tvDoctorDepartment.setText("消化科");
                ivDepartment.setBackgroundResource(R.drawable.icon_department_gastroenterology_dept);
                break;
            case Constant.department_puwaike://普外科
                tvDoctorDepartment.setText("普外科");
                ivDepartment.setBackgroundResource(R.drawable.icon_department_general_surgery_dept);
                break;
            default:
                tvDoctorDepartment.setText("");
                ivDepartment.setBackgroundResource(R.drawable.icon_department_pediatrics);
                break;
        }

        btnOpenAbstrace = (Button)findViewById(R.id.btn_abstract_open);//简介展开与关闭
        llAbstract = (LinearLayout)findViewById(R.id.ll_doctor_detail_abstract);//简介
        btnStartConsult = (Button)findViewById(R.id.btn_start_consult);//开始咨询
        btnMore = (Button)findViewById(R.id.btn_doctor_details_other);//输入时，选择更多
        llConsultBody = (LinearLayout)findViewById(R.id.ll_consult_body);//咨输入咨询内容
        llRelevantContent = (RelativeLayout)findViewById(R.id.ll_relevant_content);//咨输入咨询内容时选择相关内容
        btnPicture = (Button)findViewById(R.id.btn_picture);//选择照片
        btnPhotograph = (Button)findViewById(R.id.btn_photograph);//拍照
        btnHistory = (Button)findViewById(R.id.btn_history);//历史数据
        btnRecord = (RecordButton)findViewById(R.id.btn_record);//语音
        btnDoctorDetailsVoice = (Button)findViewById(R.id.btn_doctor_details_voice);//语音
        lvChat = (ListView)findViewById(R.id.lv_chat);//聊天
        etConsult = (EditText)findViewById(R.id.et_consult);//输入
        ivLine = (ImageView)findViewById(R.id.iv_line);//输入框下边的直线
        //etConsult.setImeOptions(EditorInfo.IME_ACTION_SEND);
        etConsult.addTextChangedListener(new TextWatcher() {



            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(s.length() > 0){
                    btnMore.setBackgroundResource(R.drawable.shape_doctor_detail_start_consult);
                    isSend = true;
                    btnMore.setText("发送");
                }else {
                    btnMore.setText(null);
                    btnMore.setBackgroundResource(R.drawable.icon_doctor_details_more);
                    isSend = false;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        btnOpenAbstrace.setOnClickListener(this);
        btnStartConsult.setOnClickListener(this);
        btnMore.setOnClickListener(this);

        btnPicture.setOnClickListener(this);
        btnPhotograph.setOnClickListener(this);
        btnHistory.setOnClickListener(this);
        btnDoctorDetailsVoice.setOnClickListener(this);

        initAdapter();
        //发送语音
        btnRecord.setOnFinishedRecordListener(new OnFinishedRecordListener() {

            @Override
            public void onFinishedRecord(String audioPath) {
                // TODO Auto-generated method stub
                sendMsg(audioPath,3);
            }
        });

        switch (doctor.getdId()) {
            case 0:
                imageLoadInstance.displayFromDrawable(R.drawable.head_1, ivHead);
                break;
            case 1:
                imageLoadInstance.displayFromDrawable(R.drawable.head_2, ivHead);
                break;
            case 2:
                imageLoadInstance.displayFromDrawable(R.drawable.head_3, ivHead);
                break;
            case 3:
                imageLoadInstance.displayFromDrawable(R.drawable.head_4, ivHead);
                break;
            case 4:
                imageLoadInstance.displayFromDrawable(R.drawable.head_5, ivHead);
                break;
            case 5:
                imageLoadInstance.displayFromDrawable(R.drawable.head_6, ivHead);
                break;
            default:
                // icon_doctor_avader
                imageLoadInstance.displayFromDrawable(R.drawable.icon_doctor_avader, ivHead);
                break;
        }

    }
    /**
     *
     * @param msg
     * @param i
     */
    private void sendMsg(String msg, int i) {
        ChatMsgEntity chat = new ChatMsgEntity();
        chat.setName("李四");
        chat.setDate("2017年07月05日 13:45");
        chat.setMsgType(i);
        chat.setMessage(msg);
        chat.setComMeg(false);
        msgList.add(chat);
        chatMsgViewAdapter.notifyDataSetChanged();
        lvChat.setSelection(chatMsgViewAdapter.getCount() + 1);
    }
    /**
     * 发送图片
     * @param msg
     */
    private void sendMsgPicture(Uri msg) {
        ChatMsgEntity chat = new ChatMsgEntity();
        chat.setName("李四");
        chat.setDate("2017年07月05日 13:45");
        chat.setMsgType(2);
        chat.setPicUri(msg);
        chat.setComMeg(false);
        msgList.add(chat);
        chatMsgViewAdapter.notifyDataSetChanged();
        lvChat.setSelection(chatMsgViewAdapter.getCount()+ 1);
    }
    private void initAdapter() {
        // TODO Auto-generated method stub
        msgList = initMsgList();
        chatMsgViewAdapter = new ChatMsgViewAdapter(this,msgList);
        lvChat.setAdapter(chatMsgViewAdapter);

    }

    private List<ChatMsgEntity> initMsgList() {
        // TODO Auto-generated method stub
        ChatMsgEntity chat = new ChatMsgEntity();
        chat.setName("张三");
        chat.setDate("2017年07月05日 13:45");
        chat.setMsgType(1);
        chat.setMessage("请问有什么可以帮您");
        chat.setComMeg(true);
        ArrayList<ChatMsgEntity> arrayList = new ArrayList<ChatMsgEntity>();
        arrayList.add(chat);
        return arrayList;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.titlebar_btn_back:
                // 返回
                finish();
                break;
            case R.id.btn_abstract_open:
                //简介的展开与隐藏
                if(!isAbstract){
                    btnOpenAbstrace.setBackgroundResource(R.drawable.icon_doctor_details_open);
                    llAbstract.setVisibility(View.GONE);
                    isAbstract = true;

                }else{
                    btnOpenAbstrace.setBackgroundResource(R.drawable.icon_doctor_details_retract);
                    llAbstract.setVisibility(View.VISIBLE);
                    isAbstract = false;
                }
                break;
            case R.id.btn_start_consult:
                //开始咨询
                btnOpenAbstrace.setBackgroundResource(R.drawable.icon_doctor_details_open);
                btnOpenAbstrace.setVisibility(View.VISIBLE);
                llAbstract.setVisibility(View.GONE);
                btnStartConsult.setVisibility(View.GONE);
                isAbstract = true;
                llConsultBody.setVisibility(View.VISIBLE);
                lvChat.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_doctor_details_other:
                //选择更多
                if(isSend){
                    //发送消息
                    String msgStr = etConsult.getText().toString().trim();
                    sendMsg(msgStr,1);
                    etConsult.setText(null);
                }else{
                    if(isMore){
                        llRelevantContent.setVisibility(View.GONE);
                        isMore = false;
                    }else{
                        llRelevantContent.setVisibility(View.VISIBLE);
                        isMore = true;
                    }
                    CommonUtil.hideInputWindow(mContext);
                }
                break;
            case R.id.btn_picture:
                //选择照片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,
                        PHOTO_REQUEST_GALLERY);
                break;
            case R.id.btn_photograph:
                //拍照
                Intent intent2 = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    File appDir = new File(Environment.getExternalStorageDirectory()+ "/yijiakang");
                    if(!appDir.exists()) {
                        appDir.mkdir();
                    }
                    tempFile = new
                            File(Environment.getExternalStorageDirectory()
                            + "/yijiakang/", "yijiakang"
                            + String.valueOf(System.currentTimeMillis()) + ".jpg");
                    // Uri uri = Uri.fromFile(tempFile);
                    Uri  uri = Uri.fromFile(tempFile);
                    intent2.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent2,  PHOTO_REQUEST_CAREMA);
                } else {
                    Toast.makeText(this, "未找到存储卡，无法存储照片！",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_history:
                //历史数据
                Intent intent3 = new Intent(this,FamilyDialogActivity.class);
                startActivityForResult(intent3, PHOTO_REQUEST_SCREENSHOT);
                break;
            case R.id.btn_doctor_details_voice:
                //切换到发送语音
                if(!isVoice){
                    etConsult.setVisibility(View.GONE);
                    ivLine.setVisibility(View.GONE);
                    btnRecord.setVisibility(View.VISIBLE);
                    isVoice = true;
                }else{
                    etConsult.setVisibility(View.VISIBLE);
                    ivLine.setVisibility(View.VISIBLE);
                    btnRecord.setVisibility(View.GONE);
                    isVoice = false;
                }
                llRelevantContent.setVisibility(View.GONE);
                isMore = false;
                CommonUtil.hideInputWindow(mContext);
                break;
            case R.id.btn_record:
                //发送语音
                btnRecord.setOnFinishedRecordListener(new OnFinishedRecordListener() {

                    @Override
                    public void onFinishedRecord(String audioPath) {
                        // TODO Auto-generated method stub
                        sendMsg(audioPath,3);
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                Log.e("图片路径？？", data.getData() + "");
                sendMsgPicture(data.getData());
                // crop(uri);
            }
        } else if (requestCode == PHOTO_REQUEST_CAREMA) {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                Uri fromFile = Uri.fromFile(tempFile);
                Uri imageContentUri = getImageContentUri(mContext,tempFile);
                //crop(Uri.fromFile(tempFile));
                sendMsgPicture(imageContentUri);
            } else {
                Toast.makeText(this, "未找到存储卡，无法存储照片！",
                        Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode == PHOTO_REQUEST_SCREENSHOT){//历史数据的截图
            if(resultCode == Constant.Json_Request_Onetask){
                File imagePath = (File) data.getExtras().get("getandSaveCurrentImage");
                //                Uri uri = Uri.parse(imagePath);
                Uri imageContentUri = getImageContentUri(mContext,imagePath);
                //                Uri uri = Uri.fromFile(imagePath);
                sendMsgPicture(imageContentUri);
            }


        }
        /*else if (requestCode == PHOTO_REQUEST_CUT) {
        }
            if (data != null) {
                final Bitmap bitmap = data.getParcelableExtra("data");
                //headIcon.setImageBitmap(bitmap);
                // 保存图片到internal storage
                FileOutputStream outputStream;
                try {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    // out.close();
                    // final byte[] buffer = out.toByteArray();
                    // outputStream.write(buffer);
                    outputStream = this.openFileOutput(tempFile.getPath(),
                            Context.MODE_PRIVATE);
                    Uri catUri = Uri.fromFile(tempFile);
                    sendMsgPicture(catUri);
                    out.writeTo(outputStream);
                    out.close();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                if (tempFile != null && tempFile.exists())
                    tempFile.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }*/
    }
    /**
     * 裁剪
     * @param uri
     */
    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
    /**
     * Gets the content:// URI from the given corresponding path to a file
     *
     * @param context
     * @param imageFile
     * @return content Uri
     */
    public static Uri getImageContentUri(Context context, java.io.File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

}
