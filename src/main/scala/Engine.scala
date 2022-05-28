import Chess._
import Connect4._
import xo._
import Checkers._
import definitions._
import definitions.status._
import javafx.geometry.Insets
import javafx.scene.layout.{Background, BackgroundFill, CornerRadii}
import javafx.scene.paint.{Color, Paint}
import scalafx.application.JFXApp3
import scalafx.beans.property.{IntegerProperty, StringProperty}
import scalafx.scene.Group.sfxGroup2jfx
import scalafx.scene.control.TextField.sfxTextField2jfx
import scalafx.scene.control.{Button, TextField}
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.{HBox, VBox}
import scalafx.scene.paint.Color._
import scalafx.scene.text.{Font, Text}
import scalafx.scene.{Group, Node, Scene}
import scala.language.postfixOps
import scala.sys.exit


object Engine extends JFXApp3 {
  var dim: Int=3;   //dimension (8 for chess, 3 for xo , ... )
  var BOARDWIDTH=600.0;
  val TEXTFIELDHEIGHT = 20
  var signalingClickCount = 1       // default is 1
  var controller: controller = null
  var drawer: drawer = null
  var click_to_move:click_to_move=null
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
      click_to_move = xo_click_handler
      Engine.stage.scene = generateGameScene();
    }


    c4ICON.onMousePressed= (e:Any) => {
      drawer=connect4_drawer
      controller = connect4_controller
      dim = 7
      BOARDWIDTH = connect4_BOARDWIDTH
      signalingClickCount = 1;
      state = connect4_initial
      turn = 0;
      click_to_move=connect4_click_handler
      Engine.stage.scene = generateGameScene();
    }
    chICON.onMousePressed= (e:Any) => {
      drawer=chess_drawer
      controller = chess_controller
      dim = 8
      BOARDWIDTH = chess_BOARDWIDTH
      signalingClickCount = 2;
      state = chess_initial
      turn = 0;
      click_to_move=chess_click_handler
      Engine.stage.scene = generateGameScene();
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
    this.onKeyPressed = (key)=>{
      if(key.getCode==27){
        exit()
      }
    }
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
    new Scene(BOARDWIDTH,BOARDWIDTH+(TEXTFIELDHEIGHT+1)) {
      //game dependent values:
      //vars and vals engine related
      val t = new TextField()
      val tt = new Text(){
        this.text = s"turn: ${turn+1} "+">>Welcome to GameBuddy ... First Player's turn:"
        this.layoutX = 0
        this.layoutY = BOARDWIDTH+TEXTFIELDHEIGHT-4
        this.stroke = Blue
        this.font.value = new Font("Comic-sans",13)
      }
      sfxTextField2jfx(t).setPrefSize(BOARDWIDTH/dim,TEXTFIELDHEIGHT)
      t.layoutY = BOARDWIDTH
      t.layoutX = BOARDWIDTH-(BOARDWIDTH/dim)
      sfxTextField2jfx(t).setBackground(new Background(new BackgroundFill(Color.color(0.3,0.2,0.2,0.3),CornerRadii.EMPTY,Insets.EMPTY)))
      var ROOT = new Group();
      content = List(ROOT,t,tt);
      val drawState = (drawer: drawer, state: state) => {
        sfxGroup2jfx(ROOT).getChildren.clear()
        (drawer(state)).foreach((x) => {
          println((x).toString())
          sfxGroup2jfx(ROOT).getChildren.add(x)
        })
      }
      val inputBuffer = new StringProperty("")
      val clickcount = new IntegerProperty(this, "clickcount", 0);
      def processInput(in:String)={
        clickcount.value = 0;
        val inputresult = controller(state, in, turn%2);
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
        tt.setText(s"turn: ${turn+1} "+inputresult._3)
      }
      this.onKeyPressed = (key)=>{
        if(key.getCode.isWhitespaceKey){
          processInput(t.getText())
          t.clear()
        }
        else if(key.getCode.getCode==27){
          stage.scene = MenuScene
        }
        else {
          println(key.getCode.getCode+": unhandled key press")
        }
      }
      ROOT.onMousePressed = (ev) => {
//        val x = Math.floor(ev.getSceneX / (BOARDWIDTH / dim))
//        val y = Math.floor(ev.getSceneY / (BOARDWIDTH / dim))
        inputBuffer.value += (click_to_move(ev.getSceneX,ev.getSceneY))
        clickcount.value +=1;
        if (clickcount.value == signalingClickCount) {
          processInput(inputBuffer.value)
        };
      }
      drawState(drawer, state);
    }
  }
  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "ScalaFX Hello World"
      scene = MenuScene
    }
    stage.setResizable(false)
  }
}