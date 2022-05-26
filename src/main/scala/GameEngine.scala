import definitions.BOARDWIDTH
import scalafx.application.JFXApp3
import scalafx.event._
import scalafx.scene._
import scalafx.scene.control._
import scalafx.scene.image._
import scalafx.scene.layout._
import scalafx.scene.paint.Color._
import scalafx.scene.text.Text
import xo.{xo_drawer, Xo_initial}


object GameEngine extends JFXApp3{
  var turnFirst:Boolean=true;
  override def start(): Unit = {
    stage = new JFXApp3.PrimaryStage{
        scene = new Scene(BOARDWIDTH,BOARDWIDTH){
            fill=Grey
            title="GameBuddy"
            val xoICON= getIconXO()
            val c4ICON= getIconConnect4()
            val chICON= getIconChess()
            val ckICON= getIconCheckers()

            val buttonX=new Button("",xoICON)
            val buttonC4=new Button("",c4ICON)
            val buttonCh=new Button("",chICON)
            val buttonCk=new Button("",ckICON)

            buttonX.onAction= (e:Any) => {

            }

            buttonC4.onAction= (e:Any) => {
              println("C444444444")
            }

            buttonCh.onAction= (e:Any) => {
              println("Chesssss")
            }

            buttonCk.onAction= (e:Any) => {
              println("Checkerss")
            }


            val menuH1 = new HBox(50,buttonX,buttonC4)
            val menuH2 = new HBox(50,buttonCh,buttonCk)
            val menuAll = new VBox(50,menuH1,menuH2)
            menuAll.layoutX = 100
            menuAll.layoutY = 100

            val message = new Text(100,50," Choose a game:")
            message.setStyle("-fx-font: 30 sans-serif;")
            content = List(menuAll,message)
        }
      }
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

}
