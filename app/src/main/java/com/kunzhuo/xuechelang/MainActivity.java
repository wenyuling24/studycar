package com.kunzhuo.xuechelang;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kunzhuo.xuechelang.mapper.SimpleDataMapper;
import com.kunzhuo.xuechelang.model.User_Bean;
import com.kunzhuo.xuechelang.network.Resquest;
import com.kunzhuo.xuechelang.utils.DefaultUtils;
import com.kunzhuo.xuechelang.utils.StrictModeWrapper;
import com.kunzhuo.xuechelang.utils.ToastUtil;
import com.kunzhuo.xuechelang.utils.update.UpdateManager;
import com.kunzhuo.xuechelang.view.activity.CollectionVideo_Activity;
import com.kunzhuo.xuechelang.view.activity.FindPassword_Activity;
import com.kunzhuo.xuechelang.view.activity.Login_Activity;
import com.kunzhuo.xuechelang.view.activity.SearchMain_Activity;
import com.kunzhuo.xuechelang.view.activity.UpCoachImgRen_Activity;
import com.kunzhuo.xuechelang.view.activity.UpImage_Activity;
import com.kunzhuo.xuechelang.view.fragment.CoachMainCoach_Fragment;
import com.kunzhuo.xuechelang.view.fragment.CoachMainStu_Framgent;
import com.kunzhuo.xuechelang.view.fragment.FirstSubject_Fragment;
import com.kunzhuo.xuechelang.view.fragment.StudentMain_Fragmnet;
import com.kunzhuo.xuechelang.widget.NoScrollViewPager;
import com.kunzhuo.xuechelang.widget.SegmentedGroup;
import com.kunzhuo.xuechelang.widget.dialog.CustomDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Context context;
    private LinearLayout Wx_headerlayout; // 头像布局
    private ImageView Wxicon_Img; // 头像
    private TextView Wxnick_Name; // 昵称or用户名

    private LinearLayout main_headerlayout; // 头像布局
    private ImageView icon_Img; // 头像
    private TextView nick_Name; // 昵称or用户名

    private LinearLayout Pass_headerlayout; // 头像布局
    private ImageView Passicon_Img; // 头像
    private TextView Passnick_Name; // 昵称or用户名

    private String Uid = "";
    private int Utype = 0;
    private int Tication = 0;
    private int selectPosition = 0;
    private SegmentedGroup mGroup;
    private RadioButton stuRadioBtn;
    private RadioButton coachRadioBtn;
    private InputMethodManager inputMethodManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        StrictModeWrapper.init(context);
        AndroidApplication.activityList.add(this);
        checkForUpdate();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mGroup = (SegmentedGroup) findViewById(R.id.segmentbutton);
        stuRadioBtn = (RadioButton) findViewById(R.id.button1);
        coachRadioBtn = (RadioButton) findViewById(R.id.button2);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View viewHeader = navigationView.getHeaderView(0);

        main_headerlayout = (LinearLayout) viewHeader.findViewById(R.id.nav_selfmsg);
        main_headerlayout.setOnClickListener(this);

        icon_Img = (ImageView) viewHeader.findViewById(R.id.imageView);
        icon_Img.setOnClickListener(this);

        nick_Name = (TextView) viewHeader.findViewById(R.id.nickName);
        nick_Name.setOnClickListener(this);


        Wx_headerlayout = (LinearLayout) viewHeader.findViewById(R.id.id_BangLayout);
        Wxicon_Img = (ImageView) viewHeader.findViewById(R.id.imageBandWxView);
        Wxnick_Name = (TextView) viewHeader.findViewById(R.id.txtBangWxName);

        Pass_headerlayout = (LinearLayout) viewHeader.findViewById(R.id.id_RenLayout);
        Pass_headerlayout.setOnClickListener(this);

        Passicon_Img = (ImageView) viewHeader.findViewById(R.id.imageRenView);
        Passnick_Name = (TextView) viewHeader.findViewById(R.id.txtRenName);

        Uid = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.USER_ID);

        String userJson = DefaultUtils.getShared(context, DefaultUtils.USER, DefaultUtils.USER_JSONMSG);

        getUserPhone(userJson);

        setFragment();

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    private void checkForUpdate() {

        if (AndroidApplication.getInstance().FLAG_CHECK_UPDATE) {
            UpdateManager updateManager = new UpdateManager(this);
            updateManager.checkUpdate();
            AndroidApplication.getInstance().FLAG_CHECK_UPDATE = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Resquest.SUCCESS_MSG:
                    JSONObject jsonObject = (JSONObject) msg.obj;

                    try {
                        String code = jsonObject.getString("Code");

                        if (code.equals("0")) {
                            ToastUtil.show(context, "刷新成功");
                            java.lang.reflect.Type type = new TypeToken<List<User_Bean>>() {
                            }.getType();
                            SimpleDataMapper mapper = new SimpleDataMapper(type);

                            List<User_Bean> mList = mapper.transformCollection(jsonObject, "List");

                            User_Bean bean = mList.get(0);
                            String Uid = bean.getUID();
                            Gson gson = new Gson();
                            String msgJson = gson.toJson(bean);

                            DefaultUtils.putShared(context, msgJson, DefaultUtils.USER, DefaultUtils.USER_JSONMSG);
                            DefaultUtils.putShared(context, Uid, DefaultUtils.USER, DefaultUtils.USER_ID);

                            for (int i = 0; i < AndroidApplication.activityList.size(); i++) {

                                AndroidApplication.activityList.get(i).finish();
                            }
                            AndroidApplication.activityList.clear();

                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("index", 0);
                            startActivity(intent);

                        }
                        if (code.equals("8")) {

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case Resquest.FAILED_MSG:
                    ToastUtil.show(context, "网络异常");
                    break;
            }

            return true;
        }
    });


    private void hideSoftInput() {
        if (inputMethodManager != null) {
            View v = MainActivity.this.getCurrentFocus();
            if (v == null) {
                return;
            }

            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public void getUserPhone(String userJson) {

        if (!userJson.equals("")) {
            Gson gson = new Gson();
            User_Bean bean = gson.fromJson(userJson, User_Bean.class);

            if (!bean.getUnickname().equals("")) {
                nick_Name.setText(bean.getUnickname());
            } else {
                nick_Name.setText(bean.getUaccount().subSequence(0, 3)
                        + "****" + bean.getUaccount().subSequence(7, 11));
            }

            if (!bean.getWx_nickname().equals("")) {
                nick_Name.setText(bean.getWx_nickname());
            }

            if (!bean.getPicSrc().equals("")) {

                ImageLoader.getInstance().displayImage(bean.getHttpImg() + bean.getPicSrc(), icon_Img);

            } else {

                ImageLoader.getInstance().displayImage(bean.getWx_portrait(), icon_Img);
            }

            Utype = bean.getUtype();

            Tication = bean.getTication();
            int WeChat = bean.getWeChat();

            Pass_headerlayout.setVisibility(View.VISIBLE);
            Wx_headerlayout.setVisibility(View.VISIBLE);

            if (Tication == 0) {
                Passnick_Name.setText("未认证");
                Passicon_Img.setImageResource(R.drawable.icon_nopass);

            }
            if (Tication == 1) {
                Passnick_Name.setText("已认证");
                Passicon_Img.setImageResource(R.drawable.icon_pass);

            }

            if (WeChat == 0) {

                Wxnick_Name.setText("未绑定");
                Wxicon_Img.setImageResource(R.drawable.no_weixin);
            }
            if (WeChat == 1) {

                Wxnick_Name.setText("已绑定");
                Wxicon_Img.setImageResource(R.drawable.weixin);
            }


        } else {
            nick_Name.setText("注册/登录");
            Pass_headerlayout.setVisibility(View.GONE);
            Wx_headerlayout.setVisibility(View.GONE);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nav_selfmsg:

                if (!Uid.equals("")) {

                } else {
                    Intent intent = new Intent(context, Login_Activity.class);
                    startActivity(intent);
                }


                break;
            case R.id.imageView:
//                ToastUtil.show(context, "如果登录了显示头像未登录去登录");
                if (!Uid.equals("")) {

                } else {
                    Intent intent = new Intent(context, Login_Activity.class);
                    startActivity(intent);
                }


                break;
            case R.id.nickName:
//                ToastUtil.show(context, "去登录界面");
                if (!Uid.equals("")) {

                } else {
                    Intent intent = new Intent(context, Login_Activity.class);
                    startActivity(intent);
                }

                break;
            case R.id.id_RenLayout:

                if (!Uid.equals("")) {

                    if (Utype == 1 && Tication == 0) {

                        Intent intent = new Intent(context, UpCoachImgRen_Activity.class);
                        intent.putExtra("Uid", Uid);
                        intent.putExtra("LocationType", 0);

                        startActivity(intent);
                    }
                    if (Utype == 1 && Tication == 1) {

                        Intent intent = new Intent(context, UpCoachImgRen_Activity.class);
                        intent.putExtra("Uid", Uid);
                        intent.putExtra("LocationType", 1);

                        startActivity(intent);

                    }
                } else {
                    ToastUtil.show(context, "去登录界面");
                    Intent intent = new Intent(context, Login_Activity.class);
                    startActivity(intent);
                }


                break;
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        //加载菜单文件
        getMenuInflater().inflate(R.menu.menu_main, menu);

        final MenuItem item = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

        searchView.setQueryHint("找视频,找教练");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                Intent intent = new Intent(context, SearchMain_Activity.class);
                intent.putExtra("searchTxt", query);
                startActivity(intent);

                hideSoftInput();

                searchView.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(context, UpImage_Activity.class);
            startActivity(intent);

            return true;
        }
        if (id == R.id.action_notify) {
//            Intent intent = new Intent(context, BMap_Activity.class);
//            startActivity(intent);

            UUID uuid = UUID.randomUUID();

            System.out.println(".{" + uuid.toString() + "}");

            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_mypay) { // 我的支付
            // Handle the camera action
            if (!Uid.equals("")) {

            } else {
                ToastUtil.show(context, "请先去登录");
            }
        } else if (id == R.id.nav_bindweichat) { // 绑定微信
            if (!Uid.equals("")) {

                Resquest.getUserInformation(handler, Uid);

            } else {
                ToastUtil.show(context, "请先去登录");
            }

        }
//        else if (id == R.id.nav_invite) { // 邀请好友
//            if (!Uid.equals("")) {
//            } else {
//                ToastUtil.show(context, "请先去登录");
//            }
//
//        } else if (id == R.id.nav_watchrecord) { // 观看记录
//            if (!Uid.equals("")) {
//            } else {
//                ToastUtil.show(context, "请先去登录");
//            }
//
//        }
        else if (id == R.id.nav_mycollection) { // 我的收藏
            if (!Uid.equals("")) {

                Intent intent = new Intent(context, CollectionVideo_Activity.class);
                intent.putExtra("Uid", Uid);
                startActivity(intent);

            } else {
                ToastUtil.show(context, "请先去登录");
            }


        } else if (id == R.id.nav_password) { // 修改密码

            Intent intent = new Intent(context, FindPassword_Activity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logoff) { // 注销登录

            if (!Uid.equals("")) {
                showLogoutNoticeDialog();
            } else {
                ToastUtil.show(context, "请先去登录");
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (selectPosition == 0) {
                CustomDialog.Builder builder = new CustomDialog.Builder(this);
                builder.setMessage("确定要退出吗?").setTitle("提示");
                builder.setOnNeagtiveListener(listener);
                builder.setOnPositiveListener(listener);
                builder.create().show();
//            } else {
//                viewPager.setCurrentItem(0);
//                mGroup.check(R.id.button1);
//            }
        }


        return false;
    }


    /**
     * 展示注销界面
     */
    public void showLogoutNoticeDialog() {
        CustomDialog.Builder builder = new CustomDialog.Builder(this);
        builder.setMessage("确定要注销吗?").setTitle("提示");
        builder.setOnNeagtiveListener(listener2);
        builder.setOnPositiveListener(listener2);
        builder.create().show();
    }

    /**
     * 监听对话框里面的button点击事件
     */
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    dialog.dismiss();
                    /*退出进程*/
                    //System.exit(0);
                    /*移动到后台*/
                    moveTaskToBack(false);
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 监听对话框里面的button点击事件
     */
    DialogInterface.OnClickListener listener2 = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    dialog.dismiss();

                    DefaultUtils.getSharedClear(context, DefaultUtils.USER);

                    for (int i = 0; i < AndroidApplication.activityList.size(); i++) {

                        AndroidApplication.activityList.get(i).finish();
                    }
                    AndroidApplication.activityList.clear();

                    Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);

                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    dialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    };

    NoScrollViewPager viewPager;

    public void setFragment() {

        Intent intent = getIntent();
        int page = intent.getIntExtra("index", 0);// 第一个参数是取值的key,第二个参数是默认值

        viewPager = (NoScrollViewPager) findViewById(R.id.viewpager_fragment);

        viewPager.setNoScroll(true);// 禁止滑动

        setFragmentViewPager();

        viewPager.setCurrentItem(page);
        viewPager.setOffscreenPageLimit(2);

        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                if (checkedId == stuRadioBtn.getId()) {  // 学员

                    viewPager.setCurrentItem(0);

                } else if (checkedId == coachRadioBtn.getId()) { // 教练

                    viewPager.setCurrentItem(1);
                }
            }
        });


    }


    public void setFragmentViewPager() {

        viewPager.setAdapter(new MyFragmentPagerAdapter(
                getSupportFragmentManager()));

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                selectPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    private Fragment getFragment(int selectPosition) {
        this.selectPosition = selectPosition;

        Fragment select = null;
        switch (selectPosition) {
            case 0:
                selectPosition = 0;

                select = new StudentMain_Fragmnet();
                break;
            case 1:

                selectPosition = 1;

                if (Utype == 0) { // 我是学员

                    select = new CoachMainStu_Framgent();


                } else if (Utype == 1) { // 我是教练

                    select = new CoachMainCoach_Fragment();

                }
                break;

            default:
                select = new StudentMain_Fragmnet();
        }

        return select;
    }


    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private String[] titles = {"科目一", "科目二"};

        public MyFragmentPagerAdapter(FragmentManager fm) {
            super(fm); // 这句代码必须实现
        }

        // 返回当前项
        @Override
        public Fragment getItem(int position) {

            return getFragment(position);
        }

        // 返回总的数量
        @Override
        public int getCount() {
            return titles.length;
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return titles[position];
//        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }

    }


}
