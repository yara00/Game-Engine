import definitions.status.status
import scalafx.scene.Node

object definitions{
  type input = String
  type turn = Int
  type state = Array[Array[String]]
  type drawable = List[Node]
  type drawer = state =>drawable
  type controller = (state,input,turn)=>(state,status,String)

  object status extends Enumeration{
    type status = Value
    //for move:
    val Invalid: status.Value = Value("Invalid")
    //for game and turns
    //running
    val Player_0_turn: status.Value = Value("First Player's turn")
    val Player_1_turn: status.Value = Value("Second Player's turn")
    //ended
    val Player_0_won: status.Value = Value("First Player Won")
    val Player_1_won: status.Value = Value("Second Player Won")
    val Draw: status.Value = Value("Draw")
  }
}