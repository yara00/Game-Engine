import definitions._
import definitions.status._
import scalafx.application.JFXApp3
import scalafx.beans.property.{IntegerProperty, StringProperty}
import scalafx.scene.Group.sfxGroup2jfx
import scalafx.scene.control.Button
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.KeyCode
import scalafx.scene.input.KeyCode.Escape
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.text.Text
import scalafx.scene.{Group, Node, Scene}
import xo._

import scala.language.postfixOps


object Engine extends JFXApp3 {
  var dim: Int=3;   //dimension (8 for chess, 3 for xo , ... )
  var BOARDWIDTH:Int=600;
  var signalingClickCount = 1       // default is 1
  var controller: controller = null
  var drawer: drawer = null

  //initial value for state is game dependent too
  var state: state = null;
  var turn: turn = 0;
  def MenuScene = new Scene(600,600){
    var R = new Group()
    sfxGroup2jfx(R).getChildren.add(getBackground())
    //        Engine.stage.title="GameBuddy"
    val xoICON= getIconXO()
    val c4ICON= getIconConnect4()
    val chICON= getIconChess()
    val ckICON= getIconCheckers()
    xoICON.onMousePressed = (e)=>{
      drawer=xo_drawer
      controller = xo_controller
      dim = 3
      BOARDWIDTH = xo_BOARDWIDTH
      signalingClickCount = 1;
      state = xo_initial
      turn = 0;
      Engine.stage.scene = generateGameScene();
    }


    c4ICON.onMousePressed= (e:Any) => {
      println("C444444444")
    }
    chICON.onMousePressed= (e:Any) => {
      println("Chesssss")
    }
    ckICON.onMousePressed= (e:Any) => {
      println("Checkerss")
    }
    val menuH1 = new HBox(100,xoICON,chICON)
    val menuH2 = new HBox(100,c4ICON,ckICON)
    val menuAll = new VBox(25,menuH1,menuH2)
    menuAll.layoutX = 150
    menuAll.layoutY = 275
    sfxGroup2jfx(R).getChildren.add(menuAll)
    content = R
  }
  def getBackground(): ImageView ={
    val img = new Image("file:assets/background.png",600,600,true,true)
    val imageView = new ImageView(img)
    imageView
  }

  def getIconXO(): ImageView ={
    val img = new Image("file:assets/xo.png",100,100,false,true)
    val imageView = new ImageView(img)
    imageView
  }

  def getIconConnect4(): ImageView ={
    val img = new Image("file:assets/connect4.png",100,100,true,true)
    val imageView = new ImageView(img)
    imageView
  }

  def getIconCheckers(): ImageView ={
    val img = new Image("file:assets/checkers.png",100,100,true,true)
    val imageView = new ImageView(img)
    imageView
  }
  def getIconChess(): ImageView ={
    val img = new Image("file:assets/chess.png",100,100,true,true)
    val imageView = new ImageView(img)
    imageView
  }
  def generateGameScene:()=>Scene =()=>{
    new Scene(BOARDWIDTH,BOARDWIDTH) {
      //game dependent values:
      //vars and vals engine related
      var ROOT = new Group();
      content = ROOT;
      val drawState = (drawer: drawer, state: state) => {
        sfxGroup2jfx(ROOT).getChildren.removeAll();
        (drawer(state)).foreach((x) => {
          sfxGroup2jfx(ROOT).getChildren.add(x)
        })
      }
      val inputBuffer = new StringProperty("")
      val clickcount = new IntegerProperty(this, "clickcount", 0);

      this.onKeyPressed = (key)=>{
        key.getText match {
          case ("m"|"M")=> stage.scene = MenuScene
          case _=>println(key.getText +" pressed")
        }
      }
      ROOT.onMousePressed = (ev) => {
        val x = Math.floor(ev.getSceneX / (BOARDWIDTH / dim))
        val y = Math.floor(ev.getSceneY / (BOARDWIDTH / dim))
        inputBuffer.value += (s"${x} ${y} ")
        clickcount.value +=1;
        if (clickcount.value == signalingClickCount) {
          clickcount.value = 0;
          val inputresult = controller(state, inputBuffer.value, turn);
          inputBuffer.value = ""
          val res = inputresult._2
          res match {
            case Invalid => System.out.println("INVALID");
            case Player_0_turn => state = inputresult._1; turn += 1; drawState(drawer, state);
            case Player_1_turn => state = inputresult._1; turn += 1; drawState(drawer, state);
            case Player_0_won => println("X won");drawState(drawer, state);turn=0
            case Player_1_won => println("O won");drawState(drawer, state);turn=1
            case Draw => System.out.println("Draw");drawState(drawer, state);
            case _ => System.out.println(s"UNHANDLED CASE!");
          }
        };
//        System.out.println(s"${x} ${y} ")
      }
      drawState(drawer, state);
    }
  }
  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "ScalaFX Hello World"
//      val MenuScene = new Scene(500,500){
//        fill=Grey
////        Engine.stage.title="GameBuddy"
//        val xoICON= getIconXO()
//        val c4ICON= getIconConnect4()
//        val chICON= getIconChess()
//        val ckICON= getIconCheckers()
//
//        val buttonX=new Button("",xoICON)
//        val buttonC4=new Button("",c4ICON)
//        val buttonCh=new Button("",chICON)
//        val buttonCk=new Button("",ckICON)
//        buttonX.onAction= (e:Any) => {
//          drawer=xo_drawer
//          controller = xo_controller
//          dim = 3
//          BOARDWIDTH = xo_BOARDWIDTH
//          signalingClickCount = 1;
//          state = xo_initial
//          turn = 0;
//          Engine.stage.scene = generateGameScene();
//        }
//        buttonC4.onAction= (e:Any) => {
//          println("C444444444")
//        }
//        buttonCh.onAction= (e:Any) => {
//          println("Chesssss")
//        }
//        buttonCk.onAction= (e:Any) => {
//          println("Checkerss")
//        }
//        val menuH1 = new HBox(50,buttonX,buttonC4)
//        val menuH2 = new HBox(50,buttonCh,buttonCk)
//        val menuAll = new VBox(50,menuH1,menuH2)
//        menuAll.layoutX = 100
//        menuAll.layoutY = 100
//
//        val message = new Text(100,50," Choose a game:")
//        message.setStyle("-fx-font: 30 sans-serif;")
//        content = List(menuAll,message)
//      }
      scene = MenuScene
    }
  }
}