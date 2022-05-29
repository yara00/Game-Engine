import javafx.geometry.Insets
import javafx.scene.layout.{Background, BackgroundFill, CornerRadii}
import javafx.scene.paint.Color
import scalafx.beans.property.{IntegerProperty, StringProperty}
import scalafx.scene.Group.sfxGroup2jfx
import scalafx.scene.control.TextField
import scalafx.scene.control.TextField.sfxTextField2jfx
import scalafx.scene.paint.Color.Blue
import scalafx.scene.text.{Font, Text}
import scalafx.scene.{Group, Node, Scene}

import scala.language.postfixOps

object Engine{

  object status extends Enumeration{
    type status = Value
    //for move:
    val Invalid: status.Value = Value(">>Invalid")
    //for game and turns
    //running
    val Player_0_turn: status.Value = Value(">>First Player's turn:")
    val Player_1_turn: status.Value = Value(">>Second Player's turn:")
    //ended
    val Player_0_won: status.Value = Value(">>First Player Won!")
    val Player_1_won: status.Value = Value(">>Second Player Won!")
    val Draw: status.Value = Value(">>Draw")
  }
  type input = String
  type turn = Int
  type state = Array[Array[String]]
  type drawable = List[Node]
  type drawer = state =>drawable
  type controller = (state,input,turn)=>(state,status.status,String)
  type click_to_move = (Double,Double)=>input

  def generateGame:(drawer,controller,Int,Int,Int,state,click_to_move)=>Scene =(drawerr,controllerr,dime,BoardWidth,click_count,state_init,click_to_movee)=>{
    var state=state_init;
    val signalingClickCount=click_count
    val dim=dime
    val click_to_move: click_to_move = click_to_movee
    val drawer: drawer = drawerr
    val controller: controller = controllerr
    val BOARDWIDTH:Int=BoardWidth
    var turn=0
    def TEXTFIELDHEIGHT = 20

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
          case status.Invalid => System.out.println("INVALID");inputBuffer.value = oldinputBuffer.takeRight(l*(signalingClickCount-1)) ; drawState(drawer, state);
          case status.Player_0_turn => state = inputresult._1; turn += 1; drawState(drawer, state);
          case status.Player_1_turn => state = inputresult._1; turn += 1; drawState(drawer, state);
          case status.Player_0_won => println("X won");drawState(drawer, state);turn=0
          case status.Player_1_won => println("O won");drawState(drawer, state);turn=1
          case status.Draw => System.out.println("Draw");drawState(drawer, state);
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
}