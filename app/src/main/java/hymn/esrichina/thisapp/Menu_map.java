package hymn.esrichina.thisapp;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.FeatureQueryResult;
import com.esri.arcgisruntime.data.QueryParameters;
import com.esri.arcgisruntime.data.ShapefileFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.BackgroundGrid;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.SketchCreationMode;
import com.esri.arcgisruntime.mapping.view.SketchEditor;
import com.esri.arcgisruntime.mapping.view.SketchStyle;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleRenderer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Menu_map extends AppCompatActivity {

    private MapView mMapView;

    private DrawerLayout mDrawerLayout;
    private ListView mListView;
    private ActionBarDrawerToggle mDrawerToggle;
    private FeatureLayer featureLayer;
    private FeatureLayer featureLayer2;
    private FeatureLayer featureLayer3;
    private Callout callout;
    private SketchEditor mainSketchEditor;
    private SketchStyle mainSketchStyle;
    private boolean isSelect = true;
    List<String> dataList = new ArrayList<>();
    private List<Layer> datalLayer=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_map);
        findViews();
        initView();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //设置背景
        BackgroundGrid backgroundGrid=new BackgroundGrid();
        backgroundGrid.setColor(Color.WHITE);
        //backgroundGrid.setGridLineColor(Color.WHITE);
        backgroundGrid.setGridLineWidth(0.1f);
        mMapView.setBackgroundGrid(backgroundGrid);

        //底图
        ArcGISTiledLayer arcGISTiledLayer=new ArcGISTiledLayer("/data/local/tmp/map_1/tpk_2.tpk");
        Basemap basemap=new Basemap(arcGISTiledLayer);
        ArcGISMap map = new ArcGISMap(basemap );
        mMapView.setMap(map);


        final ShapefileFeatureTable shapefileFeatureTable = new ShapefileFeatureTable(
                "/data/local/tmp/map_1/Export_Output.shp");
        featureLayer=new FeatureLayer(shapefileFeatureTable);
        //设置动态图层的透明度
        //featureLayer.setOpacity(0.5f);
        //默认会把所以子图层都显示出来
        map.getOperationalLayers().add(featureLayer);
        mMapView.setMap(map);
        datalLayer.add(featureLayer);
        dataList.add("图层1");

        final ShapefileFeatureTable shapefileFeatureTable1 = new ShapefileFeatureTable(
                "/data/local/tmp/map_1/Export_Output1.shp");
        featureLayer2=new FeatureLayer(shapefileFeatureTable1);
        //设置动态图层的透明度
        //featureLayer.setOpacity(0.5f);
        //默认会把所以子图层都显示出来
        map.getOperationalLayers().add(featureLayer2);
        mMapView.setMap(map);
        datalLayer.add(featureLayer2);
        dataList.add("图层2");

        final ShapefileFeatureTable shapefileFeatureTable2 = new ShapefileFeatureTable(
                "/data/local/tmp/map_1/Export_Output2.shp");
        featureLayer3=new FeatureLayer(shapefileFeatureTable2);
        //设置动态图层的透明度
        //featureLayer.setOpacity(0.5f);
        //默认会把所以子图层都显示出来
        map.getOperationalLayers().add(featureLayer3);
        mMapView.setMap(map);
        datalLayer.add(featureLayer3);
        dataList.add("图层3");

        //可以设置所有子图层都不显示
        //        for (int i = 0; i < datalLayer.size(); i++) {
        //            datalLayer.get(i).setVisible(false);
        //        }

        initDrawerLayout();
        startDrawing();

        SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 1.0f);
        SimpleFillSymbol fillSymbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.YELLOW, lineSymbol);
        SimpleRenderer renderer = new SimpleRenderer(fillSymbol);
        featureLayer.setRenderer(renderer);
        featureLayer.setSelectionColor(Color.GREEN);
        featureLayer.setSelectionWidth(5);
    }


    private void initDrawerLayout() {
        setupDrawer();


        DrawerLayoutAdapter drawerLayoutAdapter = new DrawerLayoutAdapter(this, dataList,  R.layout.item);
        mListView.setAdapter(drawerLayoutAdapter);

        drawerLayoutAdapter.setMyOnCheckedChangeListener(new DrawerLayoutAdapter.MyOnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked, int position) {
                if (isChecked) { //显示
                    datalLayer.get(position).setVisible(true);
                    isSelect=true;
               } else { //不显示
                    datalLayer.get(position).setVisible(false);
                    isSelect=false;
                }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }

    private void findViews() {
        mMapView = (MapView) findViewById(R.id.map_View);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mListView = (ListView) findViewById(R.id.navList);

        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return (mDrawerToggle.onOptionsItemSelected(item)) || super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.dispose();
    }

    public void startDrawing() {

        mainSketchEditor = new SketchEditor();
        mainSketchStyle = new SketchStyle();
        mainSketchEditor.setSketchStyle(mainSketchStyle);
        mMapView.setSketchEditor(mainSketchEditor);
        mMapView.setOnTouchListener (
                new DefaultMapViewOnTouchListener(this, mMapView) {
                    @Override
                    public boolean onSingleTapConfirmed(MotionEvent e) {

                        if (isSelect == true) {
                            final Point clickPoint = mMapView.screenToLocation(new android.graphics.Point(Math.round(e.getX()), Math.round(e.getY())));
                            int tolerance = 1;

                            double mapTolerance = tolerance * mMapView.getUnitsPerDensityIndependentPixel();
                            Envelope envelope = new Envelope(clickPoint.getX() - mapTolerance, clickPoint.getY() - mapTolerance, clickPoint.getX() + mapTolerance, clickPoint.getY() + mapTolerance, mMapView.getSpatialReference());
                            QueryParameters query = new QueryParameters();
                            query.setGeometry(envelope);//设置空间几何对象
                            query.setSpatialRelationship(QueryParameters.SpatialRelationship.WITHIN);
                            final ListenableFuture<FeatureQueryResult> future = featureLayer.selectFeaturesAsync(query, FeatureLayer.SelectionMode.NEW);
                            future.addDoneListener(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        FeatureQueryResult result = future.get();
                                        //mainShapefileLayer.getFeatureTable().deleteFeaturesAsync(result);
                                        Iterator<Feature> iterator = result.iterator();
                                        Feature feature;


                                        int counter = 0;
                                        while (iterator.hasNext()) {
                                            feature = iterator.next();
                                            Map<String,Object> attributes=feature.getAttributes();
                                            for (String key:attributes.keySet()){
                                                Log.e("xyh"+key,String.valueOf(attributes.get("NAME")));

                                                initCallout(clickPoint,String.valueOf(attributes.get("NAME")));
                                                counter++;}

                                        }
                                    } catch (Exception e) {
                                        e.getCause();
                                    }
                                }
                            });
                        }

                        return super.onSingleTapConfirmed(e);
                    }
                }
        );
    }

    private void initCallout(Point point,String str){

        //设置点
        GraphicsOverlay graphicsOverlay_1=new GraphicsOverlay();
        SimpleMarkerSymbol pointSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.DIAMOND, Color.RED, 10);
        Graphic pointGraphic = new Graphic(point,pointSymbol);
        graphicsOverlay_1.getGraphics().add(pointGraphic);
        mMapView.getGraphicsOverlays().add(graphicsOverlay_1);

        //获取一个气泡
        callout = mMapView.getCallout();
        TextView tv = new TextView(this);
        tv.setText(str);
        callout.setContent(tv);
        callout.show(tv,point);
    }

    //fab控件
    private static final int DISTANCE = 300;
    private static final int DISTANCE2 = 220;

    private FloatingActionButton actionButton, actionButton1, actionButton2, actionButton3;
    private boolean mMenuOpen = false;
    private View mFlMenu;
    private void initView() {
        mFlMenu = findViewById(R.id.fl_menu);

        actionButton = (FloatingActionButton) findViewById(R.id.float_btn);
        actionButton1 = (FloatingActionButton) findViewById(R.id.float_btn1);
        actionButton2 = (FloatingActionButton) findViewById(R.id.float_btn2);
        actionButton3 = (FloatingActionButton) findViewById(R.id.float_btn3);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMenuOpen) {
                    hideMenu();
                }else {
                    showMenu();
                }
            }
        });
        actionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelect=true;
            }
        });
        actionButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSelect = false;
                featureLayer.clearSelection();
                //mainSketchEditor.stop();
                //mainSketchEditor.start(SketchCreationMode.POLYGON);
                showListDialog();
            }
        });
        actionButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void showListDialog() {
        final String[] items=new String[dataList.size()+1];
        for (int i=0;i<=dataList.size();i++) {
            if (i<dataList.size()) {
                items[i] = dataList.get(i).toString();
            }else {
                items[i]="新增图层";
            }
        }
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(Menu_map.this);
        listDialog.setTitle("选择操作图层");
        listDialog.setItems(items, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // which 下标从0开始
                // ...To-do
                //Toast.makeText(Menu_map.this,
                //        "你点击了"+dataList.get(which).toString(),
                //       Toast.LENGTH_SHORT).show();
                for (int i=0;i<datalLayer.size();i++) {
                    if (i==which) {
                        datalLayer.get(which).setVisible(true);
                    }else
                    {
                        datalLayer.get(i).setVisible(false);
                    }
                }
                if (which==dataList.size())
                    showInputDialog1();
            }
        });
        listDialog.show();
    }
    private void showInputDialog1() {
    /*@setView 装入一个EditView
     */
        final EditText editText = new EditText(Menu_map.this);
        AlertDialog.Builder inputDialog =
                new AlertDialog.Builder(Menu_map.this);
        inputDialog.setTitle("新建图层名称").setView(editText);
        inputDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataList.add(editText.getText().toString());
                    }
                }).show();
    }

    private void showMenu() {
        mMenuOpen = true;
        int x = (int) actionButton.getX();
        int y = (int) actionButton.getY();
        ValueAnimator v1 = ValueAnimator.ofInt(x, x - DISTANCE);
        v1.setDuration(500);
        v1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int l = (int) animation.getAnimatedValue();
                int t = (int) actionButton1.getY();
                int r = actionButton1.getWidth() + l;
                int b = actionButton1.getHeight() + t;
                actionButton1.layout(l, t, r, b);
            }
        });
        ValueAnimator v2x = ValueAnimator.ofInt(x, x - DISTANCE2);
        ValueAnimator v2y = ValueAnimator.ofInt(y, y - DISTANCE2);
        v2x.setDuration(500).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int l = (int) animation.getAnimatedValue();
                int t = (int) actionButton2.getY();
                int r = actionButton2.getWidth() + l;
                int b = actionButton2.getHeight() + t;
                actionButton2.layout(l, t, r, b);
            }
        });
        v2y.setDuration(500).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int t = (int) animation.getAnimatedValue();
                int l = (int) actionButton2.getX();
                int r = actionButton2.getWidth() + l;
                int b = actionButton2.getHeight() + t;
                actionButton2.layout(l, t, r, b);
            }
        });
        ValueAnimator v3 = ValueAnimator.ofInt(y, y - DISTANCE);
        v3.setDuration(500).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int t = (int) animation.getAnimatedValue();
                int l = (int) actionButton3.getX();
                int r = actionButton3.getWidth() + l;
                int b = actionButton3.getHeight() + t;
                actionButton3.layout(l, t, r, b);
            }
        });
        v1.start();
        v2x.start();
        v2y.start();
        v3.start();
    }

    private void hideMenu() {
        mMenuOpen = false;
        int x = (int) actionButton1.getX();
        ValueAnimator v1 = ValueAnimator.ofInt(x, (int) actionButton.getX());
        v1.setDuration(500);
        v1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int l = (int) animation.getAnimatedValue();
                int t = (int) actionButton1.getY();
                int r = actionButton1.getWidth() + l;
                int b = actionButton1.getHeight() + t;
                actionButton1.layout(l, t, r, b);
            }
        });
        x = (int) actionButton2.getX();
        int y = (int) actionButton2.getY();
        ValueAnimator v2x = ValueAnimator.ofInt(x, (int) actionButton.getX());
        ValueAnimator v2y = ValueAnimator.ofInt(y, (int) actionButton.getY());
        v2x.setDuration(500).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int l = (int) animation.getAnimatedValue();
                int t = (int) actionButton2.getY();
                int r = actionButton2.getWidth() + l;
                int b = actionButton2.getHeight() + t;
                actionButton2.layout(l, t, r, b);
            }
        });
        v2y.setDuration(500).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int t = (int) animation.getAnimatedValue();
                int l = (int) actionButton2.getX();
                int r = actionButton2.getWidth() + l;
                int b = actionButton2.getHeight() + t;
                actionButton2.layout(l, t, r, b);
            }
        });
        y = (int) actionButton3.getY();
        ValueAnimator v3 = ValueAnimator.ofInt(y, (int) actionButton.getY());
        v3.setDuration(500).addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int t = (int) animation.getAnimatedValue();
                int l = (int) actionButton3.getX();
                int r = actionButton3.getWidth() + l;
                int b = actionButton3.getHeight() + t;
                actionButton3.layout(l, t, r, b);
            }
        });
        v1.start();
        v2x.start();
        v2y.start();
        v3.start();
    }
}
