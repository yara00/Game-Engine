import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.input.{DragEvent, MouseEvent}
import scalafx.scene.layout.{GridPane, HBox}
import scalafx.scene.paint.Color._
import scalafx.scene.paint._
import scalafx.scene.shape.Rectangle

import scala.::
import scala.language.postfixOps
object Chess extends JFXApp3{
  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage {
      scene = new Scene(500, 500) {
        fill = Grey
        var a = 0;
        var b = 0;
        var board: List[Rectangle] = List()
        for( a <- 1 to 8){
          for( b <- 1 to 8){
          val rect = Rectangle(0+(50*a),0+(50*b),50,50)
            if(b%2==0) {
              if (a % 2 == 0) {
                rect.setFill(Color.Black)
              }
              else {
                rect.setFill(Color.White)
              }
            }
            else
              {
                if (a % 2 == 0) {
                  rect.setFill(Color.White)
                }
                else {
                  rect.setFill(Color.Black)
                }
              }
         board = board.appended(rect)
          content = board
          }
        }




      }
    }
  }
}