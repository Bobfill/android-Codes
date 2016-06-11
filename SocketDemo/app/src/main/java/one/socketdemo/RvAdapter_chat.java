package one.socketdemo;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tipsview.TipsView;

import java.util.ArrayList;
import java.util.List;

import bl.BubbleLayout;

/**
 *
 */
public class RvAdapter_chat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static LayoutInflater mLayoutInflater;
    private static  Context mContext;
    private String [] mTitle;
    private int [] mPic;
    private List<MyMessage> msglist=new ArrayList<MyMessage>();

    public enum ITEM_TYPE {
        ITEM1,
        ITEM2,
        ITEM3
    }

    public RvAdapter_chat(Context context, List<MyMessage> mlist){
        mContext=context;
        msglist=mlist;
        mLayoutInflater=LayoutInflater.from(context);
    }


//    public  static class NormalViewHolder extends RecyclerView.ViewHolder{
//        TextView mTextView;
//        CardView mCardView;
//        ImageView mImageView;
//        public NormalViewHolder(View itemView) {
//            super(itemView);
//            mTextView=(TextView)itemView.findViewById(R.id.tv_text);
//            mImageView=(ImageView)itemView.findViewById(R.id.iv_pic);
//        }
//
//
//
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.ITEM1.ordinal()) {//自己的消息
            return new Item1ViewHolder(mLayoutInflater.inflate( R.layout.item_view1, parent, false));
        } else if (viewType == ITEM_TYPE.ITEM2.ordinal()){//对方的消息
            return new Item2ViewHolder(mLayoutInflater.inflate(R.layout.item_view2, parent, false));
        } else   return new Item3ViewHolder(mLayoutInflater.inflate(R.layout.list_item, parent, false));
        //return new NormalViewHolder(mLayoutInflater.inflate(R.layout.item_view2,parent,false));
    }

    public static class Item1ViewHolder extends RecyclerView.ViewHolder{
        TextView mTextView;
        BubbleLayout mbobLayout;
        public Item1ViewHolder(View itemView) {
            super(itemView);
            mTextView=(TextView)itemView.findViewById(R.id.tv_text1);
            mbobLayout=(BubbleLayout)itemView.findViewById(R.id.bobLayout);
        }
    }

    public static class Item2ViewHolder extends RecyclerView.ViewHolder{

        TextView mTextView;
        public Item2ViewHolder(View itemView) {
            super(itemView);

            mTextView=(TextView)itemView.findViewById(R.id.tv_text2);
        }
    }

    public static class Item3ViewHolder extends RecyclerView.ViewHolder{

        TextView mTextView,notifyTextView;
        public Item3ViewHolder(View itemView) {
            super(itemView);
            notifyTextView=(TextView)itemView.findViewById(R.id.notify_text);
            mTextView=(TextView)itemView.findViewById(R.id.tv_text3);

            TipsView.create((Activity) itemView.getContext()).attach(notifyTextView //);
                    , new TipsView.DragListener() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onCancel() {
                            notifyTextView.setVisibility(View.VISIBLE);
                        }
                    });
        }
    }

    /**
     * onBindViewHolder
     * 专门用来绑定ViewHolder里的控件和数据源中position位置的数据。
     */

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Item1ViewHolder) {
            ((Item1ViewHolder) holder).mTextView.setText(msglist.get(position).getContent());
            ((Item1ViewHolder) holder).mbobLayout.setForegroundGravity(Gravity.RIGHT);

        } else if (holder instanceof Item2ViewHolder) {
            ((Item2ViewHolder) holder).mTextView.setText(msglist.get(position).getContent());
        } else  ((Item3ViewHolder) holder).mTextView.setText(msglist.get(position).getContent());
    }

    /**
     *
     * getItemCount()和ListView是一样的
     */
    @Override
    public int getItemCount() {
        return msglist.size()<=0 ? 0 : msglist.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(msglist.get(position).getContent().contains("#include"))return ITEM_TYPE.ITEM1.ordinal();
        else  if(msglist.get(position).getContent().contains("#super"))return ITEM_TYPE.ITEM2.ordinal();
        else return ITEM_TYPE.ITEM3.ordinal();
    }
}
