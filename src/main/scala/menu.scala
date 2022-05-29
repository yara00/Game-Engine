import Checkers.{checkers_BOARDWIDTH, checkers_click_handler, checkers_controller, checkers_drawer, checkers_initial}
import Chess.{chess_BOARDWIDTH, chess_click_handler, chess_controller, chess_drawer, chess_initial}
import Connect4.{connect4_BOARDWIDTH, connect4_click_handler, connect4_controller, connect4_drawer, connect4_initial}
import Engine.{generateGameScene}
import scalafx.scene.Group.sfxGroup2jfx
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.{Group, Scene}
import scalafx.scene.layout.{HBox, VBox}
import xo.{xo_BOARDWIDTH, xo_click_handler, xo_controller, xo_drawer, xo_initial}

import scala.sys.exit

object menu {
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


  def MenuScene = new Scene(600,600){
    var R = new Group()
    sfxGroup2jfx(R).getChildren.add(getBackground())
    //        Engine.stage.title="GameBuddy"
    val xoICON= getIconXO()
    val c4ICON= getIconConnect4()
    val chICON= getIconChess()
    val ckICON= getIconCheckers()

    xoICON.onMousePressed = (e)=>{
      Engine.stage.scene = generateGameScene(xo_drawer,xo_controller,3,xo_BOARDWIDTH,1,xo_initial,xo_click_handler);
    }
    c4ICON.onMousePressed= (e:Any) => {
      Engine.stage.scene = generateGameScene(connect4_drawer,connect4_controller,7,connect4_BOARDWIDTH,1,connect4_initial,connect4_click_handler);
    }
    chICON.onMousePressed= (e:Any) => {
      Engine.stage.scene = generateGameScene(chess_drawer,chess_controller,8,chess_BOARDWIDTH,2,chess_initial,chess_click_handler);
    }
    ckICON.onMousePressed= (e:Any) => {
      Engine.stage.scene = generateGameScene(checkers_drawer,checkers_controller,8,checkers_BOARDWIDTH,2,checkers_initial,checkers_click_handler);
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
}
