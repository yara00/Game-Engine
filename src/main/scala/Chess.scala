import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.text._
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.input.{DragEvent, MouseEvent}
import scalafx.scene.layout.{GridPane, HBox}
import scalafx.scene.paint.Color._
import scalafx.scene.paint._
import scalafx.scene.shape.Rectangle

import scala.::
import scala.jdk.CollectionConverters._
import scala.language.postfixOps
object Chess extends JFXApp3{
  val board_Map = Map("a8" -> 0 , "a7" -> 1 , "a6" -> 2, "a5" -> 3, "a4" -> 4, "a3" -> 5, "a2" -> 6, "a1" -> 7,
    "b8" -> 8, "b7" -> 9, "b6" -> 10, "b5" -> 11    , "b4" -> 12  , "b3" -> 13  , "b2" -> 14  , "b1" -> 15,
    "c8" -> 16, "c7" -> 17, "c6" -> 18, "c5" -> 19    , "c4" -> 20  , "c3" -> 21  , "c2" -> 22  , "c1" -> 23,
    "d8" -> 24, "d7" -> 25, "d6" -> 26, "d5" -> 27    , "d4" -> 28  , "d3" -> 29  , "d2" -> 30  , "d1" -> 31,
    "e8" -> 32, "e7" -> 33, "e6" -> 34, "e5" -> 35   , "e4" -> 36  , "e3" -> 37  , "e2" -> 38  , "e1" -> 39,
    "f8" -> 40, "f7" -> 41, "f6" -> 42, "f5" -> 43    , "f4" -> 44  , "f3" -> 45 , "f2" -> 46  , "f1" -> 47,
    "g8" -> 48, "g7" -> 49, "g6" -> 50, "g5" -> 51    , "g4" -> 52  , "g3" -> 53  , "g2" -> 54  , "g1" -> 55,
    "h8" -> 56, "h7" -> 57, "h6" -> 58, "h5" -> 59    , "h4" -> 60  , "h3" ->61   , "h2" -> 62  , "h1" -> 63,
  )
  var texts :List[Text]= List()
  var board: List[Rectangle] = List()
  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      scene = new Scene(800, 800) {

        fill = Grey
        var a = 0;
        var b = 0;

        for( a <- 1 to 8){
          for( b <- 1 to 8){
          val rect = Rectangle(0+(70*a),0+(70*b),70,70)
            if(b%2==0) {
              if (a % 2 == 0) {

                rect.setFill(Color.web("#f0d9b5"))
              }
              else {
                rect.setFill(Color.web("#b58863"))
              }
            }
            else
              {
                if (a % 2 == 0) {
                  rect.setFill(Color.web("#b58863"))
                }
                else {
                  rect.setFill(Color.web("#f0d9b5"))

                }
              }
              rect.setId(a.toString+ b.toString)

              board = board.appended(rect)
              // content = board

          }


           val c = letters(a)
          val t = new Text(board(board.length-1).x.value+25  ,board(board.length-1).y.value +120,c)
          t.setStyle("-fx-font: 30 sans-serif;")
          t.setFill(Color.Black)
         texts= texts.appended(t)

        }
        val i =0;
        for( i <- 1 to 8) {
          val n = new Text( board((56+(i-1))).x.value + 120  ,board((56+(i-1))).y.value +45 ,(9-i).toString)
          n.setStyle("-fx-font: 30 sans-serif;")
          texts= texts.appended(n)
        }


        content =  board ::: texts ::: WhiteQ()
      }
    }
  }
  def letters(n:Int): String ={
    n match{
      case 1  => return "A"
      case 2  =>return "B"
      case 3  =>return "C"
      case 4  =>return "D"
      case 5  =>return "E"
      case 6  =>return "F"
      case 7  =>return "G"
      case 8  =>return "H"
    }
  }
  def WhiteQ(): List[ImageView] = {
    var pieces: List[ImageView] = List()
    val WK = new Image("file:assets/wk.png", 70, 70, false, false)
    val WQ = new Image("file:assets/wq.png", 60, 60, true, false)
    val WB = new Image("file:assets/wb.png", 70, 70, true, false)
    val WKn = new Image("file:assets/wkn.png", 60, 60, false, false)
    val WR = new Image("file:assets/wr.png", 60, 60, false, false)
    val WP = new Image("file:assets/wp.png", 50, 50, true, false)
    //  val WP3 = new Image("file:assets/wq.png",70,70,false,false)
    //  val WP4 = new Image("file:assets/wq.png",70,70,false,false)
    //  val WP5 = new Image("file:assets/wq.png",70,70,false,false)
    //  val WP6 = new Image("file:assets/wq.png",70,70,false,false)
    //  val WP7 = new Image("file:assets/wq.png",70,70,false,false)
    //  val WP8 = new Image("file:assets/wq.png",70,70,false,false)


    val BQ = new Image("file:assets/bq.png",  58, 58, false, false)
    val BK = new Image("file:assets/bk.png",  55, 55, true, false)
    val BB = new Image("file:assets/bb.png",  50, 50, true, false)
    val BKn = new Image("file:assets/bkn.png", 60, 60, false, false)
    val BR = new Image("file:assets/br.png",  60, 60, false, false)
    val BP = new Image("file:assets/bp.png",  60, 60, true, false)


    val wk = new ImageView(WK)
    val wq = new ImageView(WQ)
    val wb1 = new ImageView(WB)
    val wb2 = new ImageView(WB)
    val wr1 =new ImageView(WR)
    val wr2 =new ImageView(WR)
    val wkn1 = new ImageView(WKn)
    val wkn2 = new ImageView(WKn)
    val wp1 = new ImageView(WP)
    val wp2 = new ImageView(WP)
    val wp3 = new ImageView(WP)
    val wp4 = new ImageView(WP)
    val wp5 = new ImageView(WP)
    val wp6 = new ImageView(WP)
    val wp7 = new ImageView(WP)
    val wp8 = new ImageView(WP)

   /////////////////
    val bk = new ImageView(BK)
    val bq = new ImageView(BQ)
    val bb1 = new ImageView(BB)
    val bb2 = new ImageView(BB)
    val br1 =new ImageView(BR)
    val br2 =new ImageView(BR)
    val bkn1 = new ImageView(BKn)
    val bkn2 = new ImageView(BKn)
    val bp1 = new ImageView(BP)
    val bp2 = new ImageView(BP)
    val bp3 = new ImageView(BP)
    val bp4 = new ImageView(BP)
    val bp5 = new ImageView(BP)
    val bp6 = new ImageView(BP)
    val bp7 = new ImageView(BP)
    val bp8 = new ImageView(BP)
    val imageView4 = new ImageView(WKn)
    val imageView5 = new ImageView(WR)
    wk.setX(board(board_Map("e1")).x.value)
    wk.setY(board(board_Map("e1")).y.value)
    wq.setX(board(board_Map("d1")).x.value+5)
    wq.setY(board(board_Map("d1")).y.value+5)
    wb1.setX(board(board_Map("c1")).x.value)
    wb1.setY(board(board_Map("c1")).y.value)
    wb2.setX(board(board_Map("f1")).x.value)
    wb2.setY(board(board_Map("f1")).y.value)
    wr1.setX(board(board_Map("a1")).x.value+5)
    wr1.setY(board(board_Map("a1")).y.value+5)
    wr2.setX(board(board_Map("h1")).x.value+5)
    wr2.setY(board(board_Map("h1")).y.value+5)
    wkn1.setX(board(board_Map("b1")).x.value+5)
    wkn1.setY(board(board_Map("b1")).y.value+5)
    wkn2.setX(board(board_Map("g1")).x.value+5)
    wkn2.setY(board(board_Map("g1")).y.value+5)
    wp1.setX(board(board_Map("a2")).x.value + 13)
    wp1.setY(board(board_Map("a2")).y.value + 10)
    wp2.setX(board(board_Map("b2")).x.value + 13)
    wp2.setY(board(board_Map("b2")).y.value + 10)
    wp3.setX(board(board_Map("c2")).x.value + 13)
    wp3.setY(board(board_Map("c2")).y.value + 10)
    wp4.setX(board(board_Map("d2")).x.value + 13)
    wp4.setY(board(board_Map("d2")).y.value + 10)
    wp5.setX(board(board_Map("e2")).x.value + 13)
    wp5.setY(board(board_Map("e2")).y.value + 10)
    wp6.setX(board(board_Map("f2")).x.value + 13)
    wp6.setY(board(board_Map("f2")).y.value + 10)
    wp7.setX(board(board_Map("g2")).x.value + 13)
    wp7.setY(board(board_Map("g2")).y.value + 10)
    wp8.setX(board(board_Map("h2")).x.value + 13)
    wp8.setY(board(board_Map("h2")).y.value + 10)
    bk.setX(board(board_Map("e8")).x.value+5)
    bk.setY(board(board_Map("e8")).y.value+5)
    bq.setX(board(board_Map("d8")).x.value+5)
    bq.setY(board(board_Map("d8")).y.value+5)
    bb1.setX(board(board_Map("c8")).x.value+9)
    bb1.setY(board(board_Map("c8")).y.value+9)
    bb2.setX(board(board_Map("f8")).x.value+9)
    bb2.setY(board(board_Map("f8")).y.value+9)
    br1.setX(board(board_Map("a8")).x.value+5)
    br1.setY(board(board_Map("a8")).y.value+5)
    br2.setX(board(board_Map("h8")).x.value+5)
    br2.setY(board(board_Map("h8")).y.value+5)
    bkn1.setX(board(board_Map("b8")).x.value+5)
    bkn1.setY(board(board_Map("b8")).y.value+5)
    bkn2.setX(board(board_Map("g8")).x.value+5)
    bkn2.setY(board(board_Map("g8")).y.value+5)
    bp1.setX(board(board_Map("a7")).x.value + 5)
    bp1.setY(board(board_Map("a7")).y.value + 5)
    bp2.setX(board(board_Map("b7")).x.value + 5)
    bp2.setY(board(board_Map("b7")).y.value + 5)
    bp3.setX(board(board_Map("c7")).x.value + 5)
    bp3.setY(board(board_Map("c7")).y.value + 5)
    bp4.setX(board(board_Map("d7")).x.value + 5)
    bp4.setY(board(board_Map("d7")).y.value + 5)
    bp5.setX(board(board_Map("e7")).x.value + 5)
    bp5.setY(board(board_Map("e7")).y.value + 5)
    bp6.setX(board(board_Map("f7")).x.value + 5)
    bp6.setY(board(board_Map("f7")).y.value + 5)
    bp7.setX(board(board_Map("g7")).x.value + 5)
    bp7.setY(board(board_Map("g7")).y.value + 5)
    bp8.setX(board(board_Map("h7")).x.value + 5)
    bp8.setY(board(board_Map("h7")).y.value + 5)
    pieces = pieces.appended(wk) ::: pieces.appended(wq) ::: pieces.appended(wp1) ::: pieces.appended(wp2) ::: pieces.appended(wp3) :::
      pieces.appended(wp4) ::: pieces.appended(wp5) ::: pieces.appended(wp6) ::: pieces.appended(wp7) ::: pieces.appended(wp8) ::: pieces.appended(bp1) ::: pieces.appended(bp2) ::: pieces.appended(bp3) :::
      pieces.appended(bp4) ::: pieces.appended(bp5) ::: pieces.appended(bp6) ::: pieces.appended(bp7) ::: pieces.appended(bp8) ::: pieces.appended(wb1) ::: pieces.appended(wb2)  :::
          pieces.appended(wr2)  :::pieces.appended(wr1) :::pieces.appended(wkn1):::pieces.appended(wkn2) ::: pieces.appended(bk) ::: pieces.appended(bq)::: pieces.appended(bb1) ::: pieces.appended(bb2)  :::
      pieces.appended(br2)  :::pieces.appended(br1) :::pieces.appended(bkn1):::pieces.appended(bkn2)
    pieces
  }
}