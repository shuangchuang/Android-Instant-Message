package com.example.wtr.makefriends.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wtr.makefriends.R;
import com.example.wtr.makefriends.application.MyApplication;
import com.example.wtr.makefriends.bean.PeopleItem;
import com.example.wtr.makefriends.service.XMPPService;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


public class ShowUserActivity extends Activity implements View.OnClickListener{
    private TextView name;
    private TextView age;
    private TextView realName;
    private ImageView portrait;
    private TextView introduce;
    private TextView occupation;
    private TextView location;
    private TextView contactWay;
    private TextView bloodyType;
    private TextView education;
    private TextView email;
    private TextView nativePlace;
    private TextView constellation;
    private RelativeLayout settingButton;
    private PeopleItem theUserItem;
    private TextView existButton;

    final private DisplayImageOptions options = getSimpleOptions();
    final private ImageLoader imageLoader = ImageLoader.getInstance();
    private ImageLoadingListener animateFirstListener =
            new AnimateFirstDisplayListener();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_user);
        portrait = (ImageView)findViewById(R.id.user_portrait);
        name =(TextView)findViewById(R.id.user_name);
        age = (TextView)findViewById(R.id.user_age);
        realName = (TextView)findViewById(R.id.the_user_real_name);
        introduce = (TextView)findViewById(R.id.user_signature);
        settingButton = (RelativeLayout)findViewById(R.id.user_setting);
        theUserItem = MyApplication.getMyApplication().getUser();
        existButton = (TextView)findViewById(R.id.user_exist_button);
        occupation = (TextView)findViewById(R.id.the_user_occupation);
        location = (TextView)findViewById(R.id.the_user_location);
        contactWay = (TextView)findViewById(R.id.the_user_contact_way);
        bloodyType = (TextView)findViewById(R.id.the_user_bloody_type);
        education = (TextView)findViewById(R.id.the_user_education);
        email = (TextView)findViewById(R.id.the_user_email);
        nativePlace = (TextView)findViewById(R.id.the_user_native_place);
        constellation = (TextView)findViewById(R.id.the_user_constellation);
        settingButton.setOnClickListener(this);
        existButton.setOnClickListener(this);
        initView();
    }


    private void initView(){
        imageLoader.displayImage(theUserItem.getImage()
                ,portrait, options,animateFirstListener);
        name.setText(theUserItem.getName());
        if(("female").equals(theUserItem.getSex()))
            age.setText("♀"+ theUserItem.getAge());
        else
            age.setText("♂"+ theUserItem.getAge());
        realName.setText(theUserItem.getRealName());
        occupation.setText(theUserItem.getOccupation());
        location.setText(theUserItem.getLocation());
        contactWay.setText(theUserItem.getContactWay());
        bloodyType.setText(theUserItem.getBloodyType());
        education.setText(theUserItem.getEducation());
        email.setText(theUserItem.getEmail());
        nativePlace.setText(theUserItem.getNativePlace());
        constellation.setText(theUserItem.getConstellation());
        introduce.setText("  "+ theUserItem.getIntroduce());
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.user_setting:
                Intent intent = new Intent(ShowUserActivity.this,UserSettingActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.user_exist_button:
                Intent intent1 = new Intent(ShowUserActivity.this, XMPPService.class);
                stopService(intent1);
                MyApplication.getMyApplication().getConversationList().clear();
                Intent intent2 = new Intent(ShowUserActivity.this, LoginActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    String returnData = data.getStringExtra("data_return");
                    if(("true").equals(returnData)){
                        initView();
                    }
                }
                break;
            default:
                break;
        }
    }

    private DisplayImageOptions getSimpleOptions() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.default_portrait) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.drawable.default_portrait)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.default_portrait)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .build();//构建完成
        return options;
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                // 是否第一次显示
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    // 图片淡入效果
                    FadeInBitmapDisplayer.animate(imageView, 200);
                    displayedImages.add(imageUri);
                }
            }
        }
    }

}
