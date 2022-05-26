import definitions._
import definitions.status.{Draw, Invalid, Player_0_turn, Player_0_won, Player_1_turn, Player_1_won, status}
import xo._
import scalafx.application.JFXApp3
import scalafx.beans.Observable.sfxObservable2jfx
import scalafx.beans.property.{IntegerProperty, StringProperty}
import scalafx.scene.Group.sfxGroup2jfx
import scalafx.scene.Node.sfxNode2jfx
import scalafx.scene.Parent.sfxParent2jfx
import scalafx.scene.effect.BlendMode.Screen
import scalafx.scene.{Group, Node, Scene}
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color._
import scalafx.scene.shape.Rectangle
import scalafx.scene.text.Text

import scala.::
import scala.language.postfixOps
import scala.reflect.internal.util.Collections


object definitions{
  object status extends Enumeration{
    type status = Value
    //for move:
    val Invalid: status.Value = Value("Invalid")
    //for game and turns:
    //running
    val Player_0_turn: status.Value = Value("First Player's turn")
    val Player_1_turn: status.Value = Value("Second Player's turn")
    //ended
    val Player_0_won: status.Value = Value("First Player Won")
    val Player_1_won: status.Value = Value("Second Player Won")
    val Draw: status.Value = Value("Draw")
  }
  def BOARDWIDTH: Int = 600
  type input = String
  type turn = Int
  type state = Array[Array[Char]]
  type drawable = List[Node]
  type drawer = state =>drawable
  type controller = (state,input,turn)=>(state,status,String)

}
object xo {
  val Xo_initial: state = Array.fill(3,3)(' ')
  val Xo_controller: controller = (x: state, in: input, turn: turn) => {
    val getSymbol = (turn: turn) => {
      turn%2 match {
        case 0 => 'x'
        case 1 => 'o'
      }
    }
    val getStatus: (state, turn) => status = (s: state, p: turn) => {
      //noinspection SpellCheckingInspection
      val rowcount = Array.fill[Int](3)(0)
      //noinspection SpellCheckingInspection
      val colcount = Array.fill[Int](3)(0);
      new Array[Int](3)
      var full = true
      for (row <- Range(0, 3)) {
        for (col <- Range(0, 3)) {
          if (s(row)(col) == ' ') {
            full = false
          }
          if (s(row)(col) == getSymbol(p)) {
            rowcount(row) += 1
            colcount(col) += 1
          }
        }
      }
      if (rowcount.contains(3) || colcount.contains(3)) {
        turn%2 match {
          case 0 => status.Player_0_won
          case 1 => status.Player_1_won
        }
      }
      else if (full) {
        status.Draw
      }
      else {
        (turn%2) match {
          case 0 => status.Player_1_turn
          case 1 => status.Player_0_turn
        }
      }
    }

    val sel = in.split(" ")
    println("in: "+in)
    println("sel:" +sel);
    val row = sel(0).toDouble.toInt
    val col = sel(1).toDouble.toInt
    val player = getSymbol(turn)
    val returned = (null, status.Invalid, "invalid square")
    if (x(row)(col) != ' ') {
      returned
    }
    else {
      x(row)(col)=player
      (x, getStatus(x, player), "Valid")
    }
  }
  val xo_drawer:drawer=
    (x:state)=>{
    val CELLWIDTH = BOARDWIDTH / 3
    var lst: List[Node]= List()
    var t_lst:List[Node] = List()
    for{
      i <- List(0, 1, 2)
      j <- List(0, 1, 2)
    }{
      val r = new Rectangle()
      r.layoutX = j * CELLWIDTH
      r.layoutY = i * CELLWIDTH
      r.width = CELLWIDTH
      r.height = CELLWIDTH
      r.fill = White
      r.stroke = Black
      val text= if (x(i)(j)=='x') "X" else if (x(i)(j)=='o') "O" else ""
      val center_y = (j+0.5)*CELLWIDTH
      var center_x = (i+0.5)*CELLWIDTH
      if(x(i)(j)=='o')
        center_x=center_x- CELLWIDTH/40
      val gap = CELLWIDTH/5
      val t = new Text(x = center_x-CELLWIDTH/2+gap/2, y = center_y+CELLWIDTH/2-gap/2, t = text)
      t.fill = if (x(i)(j)=='x') Red else if (x(i)(j)=='o') Blue else White
      t.stroke = Black
      t.style =  s"-fx-font:normal ${CELLWIDTH-gap}pt ariel"

      lst = lst .:: (r)
      t_lst = t_lst .:: (t)
    }
    lst=lst.appendedAll(t_lst)
    lst
  }
}

object GameEngine extends JFXApp3 {
  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      title = "ScalaFX Hello World"
      scene = new Scene(600, 600) {
        //game dependent values:
        var dim: Array[Int] = Array.fill(2)(3);
        val signalingClickCount = 1 // 2 for chess ..
        val controller: controller = Xo_controller
        val drawer: drawer = xo_drawer

        //initial value for state is game dep. too
        var state: state = Xo_initial

        //vars and vals who do not care
        var ROOT = new Group();
        content = ROOT;
        val drawState = (drawer: drawer, state: state) => {
          sfxGroup2jfx(ROOT).getChildren.removeAll();
          (drawer(state)).foreach((x) => {
            sfxGroup2jfx(ROOT).getChildren.add(x)
          })
        }
        drawState(drawer, state)
        var turn: turn = 0;
        val inputBuffer = new StringProperty("")
        val clickcount = new IntegerProperty(this, "clickcount", 0);

        ROOT.onMousePressed = (ev) => {
          val x = Math.floor(ev.getSceneX / (BOARDWIDTH / dim(0)))
          val y = Math.floor(ev.getSceneY / (BOARDWIDTH / dim(1)))
          inputBuffer.value += (s"${x} ${y} ")
          clickcount.value +=1;
          if (clickcount.value == signalingClickCount) {
            clickcount.value = 0;
            val inputresult = controller(state, inputBuffer.value, turn);
            inputBuffer.value = ""

            val res = inputresult._2
            println(
              """hi
                |yo
                |""".stripMargin);
            res match {
              case Invalid => System.out.println("INVALID");
              case Player_0_turn => state = inputresult._1; turn += 1; drawState(drawer, state); println("hi")
              case Player_1_turn => state = inputresult._1; turn += 1; drawState(drawer, state); println("hii")
              case Player_0_won => System.out.println("X won");drawState(drawer, state);
              case Player_1_won => System.out.println("O won");drawState(drawer, state);
              case Draw => System.out.println("Draw");drawState(drawer, state);
              case _ => System.out.println(s"UNHANDLED CASE!");
            }
          };
          else {
            println(clickcount.value);
          }
          System.out.println(s"${x} ${y} ")

        }
      }
    }
  }
}