package aplication.codewithdeev.chatsup.interfaces;

import java.util.List;

import aplication.codewithdeev.chatsup.model.chat.Chats;

public interface OnReadChatCallBack {
    void onReadSuccess(List<Chats> list);
    void onReadFailed();
}
