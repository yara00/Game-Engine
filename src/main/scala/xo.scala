import Engine.status.status
import Engine.{click_to_move, controller, drawer, input, state, status, turn}
import javafx.scene.shape.StrokeLineJoin
import scalafx.scene.Node
import scalafx.scene.effect.Glow
import scalafx.scene.paint.Color
import scalafx.scene.paint.Color.{Black, Blue, Red, White}
import scalafx.scene.shape.Rectangle
import scalafx.scene.shape.Shape.sfxShape2jfx
import scalafx.scene.text.Text
import scalafx.scene.text.Text.sfxText2jfx

object xo {
  def xo_BOARDWIDTH = 600;
  def xo_initial = Array.fill(3,3)(" ")
  def xo_click_handler:click_to_move =(x:Double,y:Double)=>{
    println(x+" - "+y)
    s"${x.toInt/(xo_BOARDWIDTH/3)} ${y.toInt/(xo_BOARDWIDTH/3)}"
  }
  def xo_controller: controller = (x: state, in: input, turn: turn) => {
    val getSymbol = (turn: turn) => {
      (turn%2) match {
        case 0 => "x"
        case 1 => "o"
      }
    }
    val getStatus: (state, turn) => status = (s: state, p: turn) => {
      //noinspection SpellCheckingInspection
      val rowcount = Array.fill[Int](3)(0)
      //noinspection SpellCheckingInspection
      val colcount = Array.fill[Int](3)(0)
      //noinspection SpellCheckingInspection
      val diagonalcount = Array.fill[Int](2)(0)
      var full = true
      for (row <- Range(0, 3)) {
        for (col <- Range(0, 3)) {
          if (s(row)(col) == " ") {
            full = false
          }
          if (s(row)(col) == getSymbol(p)) {
            rowcount(row) += 1
            colcount(col) += 1
            if(row == col){
              diagonalcount(0) += 1
            }
            if((row+col)==2){
              diagonalcount(1) += 1
            }
          }
        }
      }
      if (rowcount.contains(3) || colcount.contains(3)||diagonalcount.contains(3)) {
        (turn%2) match {
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
    val returned = (null, status.Invalid, "invalid square")
    var invalid:Boolean = false;
    var row = 0;
    var col = 0
    try {
      row = sel(0).toDouble.toInt
      col = sel(1).toDouble.toInt
    }
    catch{
      case e:Exception=> invalid = true;
    }
    if(invalid||row>2||col>2){
      returned
    }
    else {
      if (getStatus(x, turn) == status.Player_0_won || getStatus(x, turn) == status.Player_1_won|| getStatus(x, turn) == status.Draw) {
        (xo_initial, status.Player_0_turn, "new game");
      }
      else {
        val player = getSymbol(turn)
        if (x(row)(col) != " ") {
          returned
        }
        else {
          x(row)(col) = player
          val r = getStatus(x, turn)
          (x, r,r.toString )
        }
      }
    }
  }

  def xo_drawer:drawer=
    (x:state)=>{
      val CELLWIDTH = xo_BOARDWIDTH / 3
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
        r.fill =  Color(0.1,0.1,0.1,1)
        r.stroke =  Color(0.6,0.6,0.6,1)
        val text= if (x(i)(j)=="x") "X" else if (x(i)(j)=="o") "O" else ""
        val center_y = (j+0.5)*CELLWIDTH
        var center_x = (i+0.5)*CELLWIDTH
        if(x(i)(j)=="o")
          center_x=center_x- CELLWIDTH/40
        val gap = CELLWIDTH/5
        val t = new Text(x = center_x-CELLWIDTH/2+gap/2, y = center_y+CELLWIDTH/2-gap/2, t = text)
        t.fill = if (x(i)(j)=="x") Red else if (x(i)(j)=="o") Blue else Color(0.8,0.8,0.8,1)
        t.stroke =  White
        sfxText2jfx(t).setStrokeWidth(2)
        sfxText2jfx(t).setStrokeLineJoin(StrokeLineJoin.ROUND)
        t.style =  s"-fx-font:normal ${CELLWIDTH-gap}pt ariel"
        t.effect = new Glow(1)
        sfxShape2jfx(t).setSmooth(true)
        sfxShape2jfx(r).setStrokeWidth(4)
        sfxShape2jfx(r).setSmooth(true)

        lst = lst .:: (r)
        t_lst = t_lst .:: (t)
      }
      lst=lst.appendedAll(t_lst)
      lst
    }
}