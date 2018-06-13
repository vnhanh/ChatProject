package vn.edu.hcmute.ms14110050.chatproject.module.main_screen.home;

import java.util.ArrayList;

import vn.edu.hcmute.ms14110050.chatproject.base.life_cycle.LifeCycle;
import vn.edu.hcmute.ms14110050.chatproject.model.node_users.chatter.Chatter;

/**
 * Created by Vo Ngoc Hanh on 5/30/2018.
 */

public interface HomeContract {
    interface View extends LifeCycle.View{

        void onLoadChatters(ArrayList<Chatter> chatters);
    }
}
