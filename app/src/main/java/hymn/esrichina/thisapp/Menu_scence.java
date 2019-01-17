package hymn.esrichina.thisapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.ArcGISSceneLayer;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISScene;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.view.Camera;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerSceneProperties;
import com.esri.arcgisruntime.mapping.view.SceneView;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.symbology.TextSymbol;

public class Menu_scence extends AppCompatActivity {

    private ArcGISScene scene;
    private SceneView sceneView;
    private ArcGISSceneLayer arcGISSceneLayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_scence);
        map_2();
    }
    public void map_2(){
        sceneView=findViewById(R.id.sceneView);
        SpatialReference spatialReferences= SpatialReferences.getWgs84();
        scene=new ArcGISScene();
        String theOfflineTiledLayers = "/data/local/tmp/map_1/tpk_1.tpk";
        TileCache mainTileCache = new TileCache(theOfflineTiledLayers);
        ArcGISTiledLayer mainArcGISTiledLayer = new ArcGISTiledLayer(mainTileCache);
        Basemap mainBasemap = new Basemap(mainArcGISTiledLayer);
        scene.setBasemap(mainBasemap);
        sceneView.setScene(scene);

        // create overlays with elevation modes用高程模式创建覆盖
        GraphicsOverlay drapedOverlay = new GraphicsOverlay();
        drapedOverlay.getSceneProperties().setSurfacePlacement(LayerSceneProperties.SurfacePlacement.DRAPED);
        sceneView.getGraphicsOverlays().add(drapedOverlay);

        // create a text symbol for each elevation mode为每个仰角模式创建一个文本符号
        TextSymbol drapedText = new TextSymbol(10, "DRAPED", 0xFF000000, TextSymbol.HorizontalAlignment.LEFT,
                TextSymbol.VerticalAlignment.MIDDLE);
        drapedText.setOffsetX(5f);

        // create point for graphic location为图形位置创建点(现在不知道模型在图中坐标)
        Point point = new Point(41,-39, 500, sceneView.getSpatialReference());
        SimpleMarkerSymbol redSymbol = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, 0xFFFF0000, 10);

        //将点图形和文本图形添加到相应的图形中
        drapedOverlay.getGraphics().add(new Graphic(point, redSymbol));
        drapedOverlay.getGraphics().add(new Graphic(point, drapedText));

        Camera camera = new Camera(29.5595294122,103.7342995405, 10, 300, 90, 0);
        sceneView.setViewpointCamera(camera);
        //arcGISSceneLayer=new ArcGISSceneLayer("/data/local/tmp/map_1/mm.lpk");
        arcGISSceneLayer=new ArcGISSceneLayer("/data/local/tmp/map_1/test3D4.slpk");
        scene.getOperationalLayers().add(arcGISSceneLayer);

    }
}
