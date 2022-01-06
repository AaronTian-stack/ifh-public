package Game;

import com.badlogic.gdx.utils.Array;

import Game.abstraction.GameObject;
import Tiles.Enums.Direction;

public class CollideInfo {
	public Array<Direction> directions;
	public GameObject collide;
}
