package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {
    //declaracion de las variables necesarias para la implentacion del juego
    public static Box mesh = new Box(Vector3f.ZERO,1,1,1);
    private final static Trigger TRIGGER_ROTATE = new MouseButtonTrigger(MouseInput.BUTTON_LEFT);
    private final static String MAPPING_ROTATE = "Rotate";
    public Spatial ob;
    public int x = 1;
    public Vector3f prueba;
    
   
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    @Override
    public void simpleInitApp() {
        Node Nodo= new Node("Nodo_enemigos");
        //posicionamos la camara sobre la torre
        cam.setLocation(new Vector3f(5,3,10));
        //creamos el plano el cual se realizo con un cuadrado extendido de forma que 
        //se vea plano en posicion z
        Box Plano = new Box(5, 1, 15);
        Geometry geom = new Geometry("GPlano", Plano);

        Material matP = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //matP.setColor("Color", ColorRGBA.Blue);
        matP.setTexture("ColorMap", assetManager.loadTexture("Textures/Plano.jpg"));
        geom.setMaterial(matP);
        
        //cracion de la torre de defensa al igual con una cuadro con la caracteristicas necesarias 
        //para una torre lo cual es alta en posicion y
        Box Torre = new Box(0.5f, 2, 0.5f);
        Geometry Gtorre = new Geometry("GTorre", Torre);

        Material matT = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //matT.setColor("Color", ColorRGBA.Red);
        matT.setTexture("ColorMap", assetManager.loadTexture("Textures/Torre.jpg"));
        Gtorre.setMaterial(matT);
        
        //se creo lo que seria un castillo o la pared frontal de este mismo al igual que lo anterior 
        //con una forma alargada en x, y
        Box Castillo = new Box(5, 2, 0.5f);
        Geometry Gcastillo = new Geometry("GCastillo", Castillo);

        Material matC = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //matC.setColor("Color", ColorRGBA.Green);
        matC.setTexture("ColorMap", assetManager.loadTexture("Textures/Castillo.jpg"));
        Gcastillo.setMaterial(matC);
        
        //los enemigos se son espferas de un tamaño adecuado
        Sphere Enemigos = new Sphere(50,50,1.0f);
        Geometry GEnemigos = new Geometry("GEnemigos",Enemigos);
        
        Material matE = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //matE.setColor("Color", ColorRGBA.Red);
        matE.setTexture("ColorMap", assetManager.loadTexture("Textures/Enemigo.jpg"));
        GEnemigos.setMaterial(matE);
        
        //Geometry GCE = GEnemigos.clone();
        //GCE.setName("GCE");
        
        //se crea lo que es la mira para saber donde es que se apunta
        Geometry c = this.myBox("center mark", Vector3f.ZERO, ColorRGBA.White);
        c.scale(4);
        c.setLocalTranslation(settings.getWidth()/2,settings.getHeight()/2,0);
        guiNode.attachChild(c);
        
        inputManager.addMapping(MAPPING_ROTATE, TRIGGER_ROTATE);
        
        inputManager.addListener(analogListener, new String[]{MAPPING_ROTATE});
        
        geom.move(0,-4,5);
        Gtorre.move(5,-1,10);
        Gcastillo.move(0,-1,12);
        GEnemigos.move(0,-2.2f,-10);
        
        Nodo.attachChild(GEnemigos);

        rootNode.attachChild(geom);
        rootNode.attachChild(Gtorre);
        rootNode.attachChild(Gcastillo);
        rootNode.attachChild(Nodo);       
    }
   
    private final AnalogListener analogListener = new AnalogListener(){
        @Override
        public void onAnalog(String name,float intensity, float tpf){
            if(name.equals(MAPPING_ROTATE)){
                CollisionResults results = new CollisionResults();
                Ray ray = new Ray(cam.getLocation(), cam.getDirection());
                rootNode.collideWith(ray,results);
                //rootNode.collideWith(rayo,result);
                if(results.size()>0){
                    Geometry target = results.getClosestCollision().getGeometry();
                    //Geometry targets = result.getClosestCollision().getGeometry();
                    if(target.getName().equals("GEnemigos")){
                        Enemigos(tpf);
                        rootNode.detachChildNamed("Nodo_enemigos");
                        ob = null;
                        x++;
                        System.out.println("Valor de X"+x);
                    }               
                    if(x==35){
                        x=1;
                        Ganaste();
                        rootNode.detachChildNamed("Nodo_enemigos");
                    }
                    if(target.getName().equals("Gaceptar")){
                        x=1;
                        rootNode.detachChildNamed("Gfondo");
                        rootNode.detachChildNamed("Gaceptar");
                        rootNode.detachChildNamed("Gnegar");
                        rootNode.detachChildNamed("Gmensaje");
                        Enemigos(tpf);                     
                    }
                    if(target.getName().equals("Gnegar")){
                       salir();
                    }
                }
            }
        }
    };
    private void salir(){
        this.stop();
    }
    public void Enemigos(float tpf){
        Node Nodo= new Node("Nodo_enemigos");
        Sphere Enemigos = new Sphere(50,50,1);
        Geometry CEO = new Geometry("GEnemigos",Enemigos);
        
        Material matE = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //matE.setColor("Color", ColorRGBA.Red);
        matE.setTexture("ColorMap", assetManager.loadTexture("Textures/Enemigo.jpg"));
        CEO.setMaterial(matE);
        
        CEO.move(0.6f,-2.2f,-10);
        
        Nodo.attachChild(CEO);
               
        rootNode.attachChild(Nodo); 
    }
    public void Perdiste(){
        Box fondo = new Box(0.5f, 5, 8);
        Geometry Gfondo = new Geometry("Gfondo", fondo);

        Material matf = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //matC.setColor("Color", ColorRGBA.Green);
        matf.setTexture("ColorMap", assetManager.loadTexture("Textures/perdiste.jpg"));
        Gfondo.setMaterial(matf);
        
        Box mensaje = new Box(0.5f, 2, 6);
        Geometry Gmensaje = new Geometry("Gmensaje", mensaje);

        Material matm = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //matm.setColor("Color", ColorRGBA.Red);
        matm.setTexture("ColorMap", assetManager.loadTexture("Textures/Mperdiste.jpg"));
        Gmensaje.setMaterial(matm);
        
        Box Aceptar = new Box(0.5f, 1, 2);
        Geometry Gaceptar = new Geometry("Gaceptar", Aceptar);

        Material matA = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //matA.setColor("Color", ColorRGBA.Green);
        matA.setTexture("ColorMap", assetManager.loadTexture("Textures/Yes.jpg"));
        Gaceptar.setMaterial(matA);
        
        Box Negar = new Box(0.5f, 1, 2);
        Geometry Gnegar = new Geometry("Gnegar", Negar);

        Material matN = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //matN.setColor("Color", ColorRGBA.Green);
        matN.setTexture("ColorMap", assetManager.loadTexture("Textures/NO.jpg"));
        Gnegar.setMaterial(matN);
        
        Gfondo.move(-6,2,2);
        Gmensaje.move(-5.9f,4,2);
        Gaceptar.move(-5.9f,0.5f,5);
        Gnegar.move(-5.9f,0.5f,-1);
        
        rootNode.attachChild(Gfondo);
        rootNode.attachChild(Gmensaje);
        rootNode.attachChild(Gaceptar);
        rootNode.attachChild(Gnegar);
    }
    
    public void Ganaste(){
        Box fondo = new Box(0.5f, 5, 8);
        Geometry Gfondo = new Geometry("Gfondo", fondo);

        Material matf = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //matC.setColor("Color", ColorRGBA.Green);
        matf.setTexture("ColorMap", assetManager.loadTexture("Textures/perdiste.jpg"));
        Gfondo.setMaterial(matf);
        
        Box mensaje = new Box(0.5f, 2, 6);
        Geometry Gmensaje = new Geometry("Gmensaje", mensaje);

        Material matm = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //matm.setColor("Color", ColorRGBA.Green);
        matm.setTexture("ColorMap", assetManager.loadTexture("Textures/Mganaste.jpg"));
        Gmensaje.setMaterial(matm);
        
        Box Aceptar = new Box(0.5f, 1, 2);
        Geometry Gaceptar = new Geometry("Gaceptar", Aceptar);

        Material matA = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //matA.setColor("Color", ColorRGBA.Green);
        matA.setTexture("ColorMap", assetManager.loadTexture("Textures/Yes.jpg"));
        Gaceptar.setMaterial(matA);
        
        Box Negar = new Box(0.5f, 1, 2);
        Geometry Gnegar = new Geometry("Gnegar", Negar);

        Material matN = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        //matN.setColor("Color", ColorRGBA.Green);
        matN.setTexture("ColorMap", assetManager.loadTexture("Textures/NO.jpg"));
        Gnegar.setMaterial(matN);
        
        Gfondo.move(-6,2,2);
        Gmensaje.move(-5.9f,4,2);
        Gaceptar.move(-5.9f,0.5f,5);
        Gnegar.move(-5.9f,0.5f,-1);
        
        rootNode.attachChild(Gfondo);
        rootNode.attachChild(Gmensaje);
        rootNode.attachChild(Gaceptar);
        rootNode.attachChild(Gnegar);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
        if(ob==null){
            System.err.println("No esta asignado el objeto¡");
            ob = rootNode.getChild("Nodo_enemigos");
        }
        else
        {               
        ob.move(0,0,tpf*x);
        prueba = ob.getLocalTranslation();
        int h = (int) prueba.z;
            if(h==20){
                Perdiste();
                rootNode.detachChildNamed("Nodo_enemigos");
            }
        //System.out.println("para saber su localizacion"+prueba);
        //System.out.println("el valor de Z es:"+h);
        }        
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private Geometry myBox(String name, Vector3f loc, ColorRGBA color){
        Geometry geom = new Geometry(name, mesh);
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.setColor("Color", color);
        geom.setMaterial(mat);
        geom.setLocalTranslation(loc);
        return geom;
    }
}

