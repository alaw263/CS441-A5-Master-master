package com.cs441_app;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;

import java.util.ArrayList;

public class UserManager implements AdapterView.OnItemClickListener{

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private static UserManager instance = null;
    private static Context context;
    private static View navView;

    private static ArrayList<Task> list;

    public void populateNavList(Context c, View v){
       context = c;
       navView = v;
        mDrawerList = (ListView)navView.findViewById(R.id.navList);
        String[] osArray = { "My Calendar", "My Groups", "Group Finder", "ToDo List", "Sign Out" };
        mAdapter = new ArrayAdapter<String>(c, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);
        mDrawerList.setOnItemClickListener(this);

    }

    private UserManager(){
        list = new ArrayList();
    }

    public static UserManager getInstance(){
        if(instance == null){
            instance = new UserManager();
        }
        return instance;
    }

    public void populateCalendar(String BlockID){
        list.clear();
        MainActivity.dh.readBlock(MainActivity.user.getUserID(), "", BlockID, new Group(), false);
        MainActivity.dh.readGroups_BySync(MainActivity.user.getUserID(), BlockID);
    }

    public static void updateTaskList(ArrayList<Task> t_list){
        list.addAll(t_list);
        MainActivity.updateTaskList(list);
    }

    public static void updateGroupList(ArrayList<Group> g_list, String BlockID){
        for(int i = 0; i < g_list.size(); i++){
            MainActivity.dh.readBlock(MainActivity.user.getUserID(), g_list.get(i).getGroupID(), BlockID, g_list.get(i), true);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intentNav;

        switch (position) {
            case 0:
                MainActivity.groupview = false;
                intentNav = new Intent(context,
                        MainActivity.class);
                context.startActivity(intentNav);
                break;
            case 1:
                intentNav = new Intent(context,
                        GroupList.class);
                context.startActivity(intentNav);
                break;
            case 2:
                intentNav = new Intent(context,
                        GroupFinder.class);
                context.startActivity(intentNav);
                break;
            case 3:
                intentNav = new Intent(context,
                        ToDoActivity.class);
                System.out.println(intentNav.toString());
                context.startActivity(intentNav);
                break;
            case 4:
                MainActivity.groupview = false;
                intentNav = new Intent(context,
                        MainActivity.class);
                final Intent intent = intentNav;
                AuthUI.getInstance()
                        .signOut(context)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                                MainActivity.user = null;
                                MainActivity.setUsercreated(false);
                                MainActivity.setLogin(false);
                                context.startActivity(intent);
                            }
                        });
                break;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.


        }
    }
}
