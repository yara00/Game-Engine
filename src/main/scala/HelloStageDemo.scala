import scalafx.Includes.when
import scalafx.application.JFXApp3
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.ListView
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.{Background, HBox}
import scalafx.scene.paint.Color._
import scalafx.scene.paint._
import scalafx.scene.text.Text
import scalafx.scene.layout.Background

object HelloStageDemo extends JFXApp3 {


  override def start(): Unit = {
      stage = new  JFXApp3.PrimaryStage{

      }
  }

}

//
//  stage = new JFXApp3.PrimaryStage {
//    //    initStyle(StageStyle.Unified)
//    title = "ScalaFX Hello World"
//    scene = new Scene {
//      fill = Color.rgb(38, 38, 38)
//      content = new HBox {
//        padding = Insets(50, 80, 50, 80)
//        children = Seq(
//          new Text {
//            text = "3amo"
//            style = "-fx-font: normal bold 100pt sans-serif"
//            fill = new LinearGradient(
//              endX = 0,
//              stops = Stops(Red, Blue))
//            effect = new DropShadow {
//              //                color = DarkGray
//              color <== when(hover) choose DarkGray otherwise Yellow
//
//              radius = 15
//              spread = 0.25
//            }
//          },
//          new Text {
//            text = "FX"
//            style = "-fx-font: italic bold 100pt sans-serif"
//            fill = new LinearGradient(
//              endX = 0,
//              stops = Stops(White, Black)
//            )
//
//          }
//        )
//      }
//    }
//  }
//}


//  override def start(): Unit = {
//    stage = new JFXApp3.PrimaryStage {
//      title.value = "Hello main Stage"
//      width = 600
//      height = 600
//      scene = new Scene {
//        fill = Grey
//        content = new Rectangle {
//          x = 150
//          y = 150
//          width = 300
//          height = 300
//          fill <== when(hover) choose Green otherwise Red
//        }
//      }
//    }
//  }