import Chess._
import Connect4._
import xo._
import Checkers._
import definitions.{click_to_move, controller, drawer, state, _}
import definitions.status._
import javafx.geometry.Insets
import javafx.scene.layout.{Background, BackgroundFill, CornerRadii}
import javafx.scene.paint.{Color, Paint}
import menu.MenuScene
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

  def generateGameScene:(drawer,controller,Int,Int,Int,state,click_to_move)=>Scene =(drawer,controller,dim,BoardWidth,click_count,state_init,click_to_move)=>{
    this.state=state_init;
    this.signalingClickCount=click_count
    this.dim=dim
    this.click_to_move=click_to_move
    this.drawer=drawer
    this.controller=controller
    this.BOARDWIDTH=BoardWidth
    this.turn=0

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
          sfxGroup2jfx(ROOT).getChildren.add(x)
        })
      }
      val inputBuffer = new StringProperty("")
      val clickcount = new IntegerProperty(this, "clickcount", 0);
      def processInput(in:String)={
        clickcount.value = 0;
        val inputresult = controller(state, in, turn%2);
        val l = click_to_move(1,1).length
        val res = inputresult._2
        val oldinputBuffer= inputBuffer.value
        inputBuffer.value =""
        res match {
          case Invalid => System.out.println("INVALID");inputBuffer.value = oldinputBuffer.takeRight(l*(signalingClickCount-1)) ; drawState(drawer, state);
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
        else {
          println(key.getCode.getCode+": unhandled key press")
        }
      }
      ROOT.onMousePressed = (ev) => {
        inputBuffer.value += (click_to_move(ev.getSceneX,ev.getSceneY))
        clickcount.value +=1;
        val l = click_to_move(1,1).length
        if (clickcount.value == signalingClickCount|| inputBuffer.value.length==l*signalingClickCount) {
          processInput(inputBuffer.value)
        };
      }
      drawState(drawer, state);
    }
  }
  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "GameBuddy"
      scene = MenuScene
    }
    stage.setResizable(false)
  }
}