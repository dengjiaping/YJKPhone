package com.sinosoft.fhcs.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sinosoft.fhcs.android.R;
import com.sinosoft.fhcs.android.activity.PhotoViewActivity;
import com.sinosoft.fhcs.android.customview.CircleImageView;
import com.sinosoft.fhcs.android.entity.ChatMsgEntity;
import com.sinosoft.fhcs.android.util.LoadLocalImageUtil;
import com.sinosoft.fhcs.android.util.MediaManager;

import java.util.List;


/**
 * 消息ListView的Adapter
 *
 * @author way
 */
public class ChatMsgViewAdapter extends BaseAdapter {

    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;// 收到对方的消息
        int IMVT_TO_MSG = 1;// 自己发送出去的消息
    }

    private static final int ITEMCOUNT = 2;// 消息类型的总数
    private List<ChatMsgEntity> coll;// 消息对象数组
    private LayoutInflater mInflater;
    private static Context mCntext;
    ViewHolder viewHolder = null;

    public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> coll) {
        this.coll = coll;
        this.mCntext = context;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return coll.size();
    }

    public Object getItem(int position) {
        return coll.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    /**
     * 得到Item的类型，是对方发过来的消息，还是自己发送出去的
     */
    public int getItemViewType(int position) {
        ChatMsgEntity entity = coll.get(position);

        if (entity.isComMeg()) {//收到的消息
            return IMsgViewType.IMVT_COM_MSG;
        } else {//自己发送的消息
            return IMsgViewType.IMVT_TO_MSG;
        }
    }

    /**
     * Item类型的总数
     */
    public int getViewTypeCount() {
        return ITEMCOUNT;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        final ChatMsgEntity entity = coll.get(position);
        boolean isComMsg = entity.isComMeg();

        if (convertView == null) {
            if (isComMsg) {
                convertView = mInflater.inflate(
                        R.layout.chatting_item_msg_text_left, null);
            } else {
                convertView = mInflater.inflate(
                        R.layout.chatting_item_msg_text_right, null);
            }
            viewHolder = new ViewHolder();
            /* viewHolder.tvSendTime = (TextView) convertView
                  .findViewById(R.id.tv_sendtime);
          viewHolder.tvUserName = (TextView) convertView
                  .findViewById(R.id.tv_username);  */
            viewHolder.tvContent = (TextView) convertView
                    .findViewById(R.id.tv_msg);
            viewHolder.ivItem = (ImageView) convertView
                    .findViewById(R.id.iv_item_left);
            viewHolder.ivItemHead = (CircleImageView) convertView
                    .findViewById(R.id.iv_item_head);
            viewHolder.isComMsg = isComMsg;

            viewHolder.ivItem.setTag(position);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
       /* if(isComMsg){//接受到的消息
            //显示医生头像

        }else{

        }*/
        switch (entity.getMsgType()) {
            case 1://文字
                viewHolder.ivItem.setVisibility(View.GONE);
                viewHolder.tvContent.setVisibility(View.VISIBLE);
                //viewHolder.tvSendTime.setText(entity.getDate());
                //viewHolder.tvUserName.setText(entity.getName());
                viewHolder.tvContent.setText(entity.getMessage());
                break;
            case 2://图片
                viewHolder.ivItem.setVisibility(View.VISIBLE);
                viewHolder.tvContent.setVisibility(View.GONE);
                // 加载图片
                LoadLocalImageUtil.getInstance().displayFromContent(entity.getPicUri().getPath(),  viewHolder.ivItem);
                break;
            case 3://语音
                // 播放音频
                viewHolder.ivItem.setVisibility(View.VISIBLE);
                LoadLocalImageUtil.getInstance().displayFromDrawable(R.drawable.chat_sound_item, viewHolder.ivItem);
                // viewHolder.ivItem.setBackgroundResource(R.drawable.chat_sound_item);
                viewHolder.tvContent.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        viewHolder.setOnclick(coll,position);
        return convertView;
    }

    static class ViewHolder {
        public CircleImageView ivItemHead;//头像
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;
        public ImageView ivItem;
        public boolean isComMsg = true;
        public void setOnclick(final List<ChatMsgEntity> list, final int position) {
            // TODO Auto-generated method stub
            ivItem.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    if(list.get(position).getMsgType() == 3){//3 播放语音
                        //if((Integer)ivItem.getTag() == position){
                        Log.i("当前播放的音频文件位置==", list.get(position).getMessage());
                        System.out.println("当前播放的音频文件位置==" + list.get(position).getMessage());
                        // 播放帧动画
                        ivItem.setBackgroundResource(R.drawable.play_anim);
                        AnimationDrawable anim = (AnimationDrawable) ivItem.getBackground();
                        anim.start();
                        // 播放音频
                        MediaManager.playSound(list.get(position).getMessage(), new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                ivItem.setBackgroundResource(R.drawable.chat_sound_item);
                            }
                        });
                        // }
                    }else if(list.get(position).getMsgType() == 2){//放大查看图片
                        Intent  intent = new Intent(mCntext,PhotoViewActivity.class);
                        intent.putExtra("imagetPath", list.get(position).getPicUri());
                        mCntext.startActivity(intent);
                    }

                }
            });
        }
    }

}  
