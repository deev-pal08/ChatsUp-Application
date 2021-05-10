package aplication.codewithdeev.chatsup.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import aplication.codewithdeev.chatsup.R;
import aplication.codewithdeev.chatsup.common.Common;
import aplication.codewithdeev.chatsup.model.chat.Chats;
import aplication.codewithdeev.chatsup.tools.AudioService;
import aplication.codewithdeev.chatsup.view.activities.display.ViewImageActivity;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {
    private List<Chats> list;
    private Context context;
    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private FirebaseUser firebaseUser;
    private ImageButton tmpBtnPlay;
    private AudioService audioService;
    private byte encryptionKey[] = {9,115,51,86,105,4,-31,-23,-68,88,17,20,3,-105,119,-53};
    private Cipher cipher,decipher;
    private SecretKeySpec secretKeySpec;

    public ChatsAdapter(List<Chats> list, Context context) {
        this.list = list;
        this.context = context;
        this.audioService = new AudioService(context);
    }

    public void setList(List<Chats> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_LEFT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        }else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textMessage;
        private LinearLayout layoutText, layoutImage, layoutVoice;
        private ImageView imageMessage;
        private ImageButton btnPlay;
        private ViewHolder tmpHolder;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.tv_text_message);
            layoutImage = itemView.findViewById(R.id.layout_image);
            layoutText = itemView.findViewById(R.id.layout_text);
            imageMessage = itemView.findViewById(R.id.image_chat);
            layoutVoice = itemView.findViewById(R.id.layout_voice);
            btnPlay = itemView.findViewById(R.id.btn_play_chat);
        }
        void bind(final Chats chats){
            //Check chat type..

            switch (chats.getType()){
                case "TEXT" :
                    layoutText.setVisibility(View.VISIBLE);
                    layoutImage.setVisibility(View.GONE);
                    layoutVoice.setVisibility(View.GONE);

                    textMessage.setText(AESDecryptionMethod(chats.getTextMessage()));

                    break;
                case "IMAGE" :
                    layoutText.setVisibility(View.GONE);
                    layoutImage.setVisibility(View.VISIBLE);
                    layoutVoice.setVisibility(View.GONE);

                    Glide.with(context).load(chats.getUrl()).into(imageMessage);
                    break;
                case "VOICE" :
                    layoutText.setVisibility(View.GONE);
                    layoutImage.setVisibility(View.GONE);
                    layoutVoice.setVisibility(View.VISIBLE);

                    layoutVoice.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (tmpBtnPlay!=null){
                                tmpBtnPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
                            }

                            btnPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_pause_circle_filled_24));
                            audioService.playAudioFromUrl(chats.getUrl(), new AudioService.OnPlayCallBack() {
                                @Override
                                public void onFinished() {
                                    btnPlay.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_play_circle_filled_24));
                                }
                            });

                            tmpBtnPlay = btnPlay;

                        }
                    });

                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (list.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;
        } else
        {
            return MSG_TYPE_LEFT;
        }
    }
    private String AESDecryptionMethod(String readMsg){
        try {
            cipher = Cipher.getInstance("AES");
            decipher = Cipher.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        secretKeySpec = new SecretKeySpec(encryptionKey,"AES");

        String readyMsg = null;
        byte decryptedMsg[];

        try {
            decipher.init(Cipher.DECRYPT_MODE,secretKeySpec);
            decryptedMsg = decipher.doFinal(readMsg.getBytes("ISO-8859-1"));
            readyMsg = new String(decryptedMsg);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return readyMsg;
    }
}

