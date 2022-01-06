package Tiles;

public class Enums {
	
  public enum GameObjectType {
    Actor, Solid, Rope;
  }

  public enum Direction {
    NONE, Left, Right, Top, Bottom;
  }
  
  public enum TileType {
    OUTOFBOUNDS, Empty, Solid, SlopeRight, SlopeLeft;
  }
  
  public enum Tile {
    OUTOFBOUNDS, Null, Empty, Chunk, Debug, Dirt, DirtSlopeRight, DirtSlopeLeft, Cave, CaveSlopeRight, CaveSlopeLeft;
  }
  
  public enum DrawMode {
    Brush, Fill, Line, Select;
  }
  
  public enum State {
    Idle, Run, Jump, Fall, CrouchWalk, CrouchIdle, Climb, ClimbIdle, Fly;
  }
  
}
