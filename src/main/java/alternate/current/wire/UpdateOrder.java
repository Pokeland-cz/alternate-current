package alternate.current.wire;

import java.util.Locale;

import alternate.current.wire.WireHandler.Directions;

public enum UpdateOrder {

	HORIZONTAL_FIRST_OUTWARD(
		new int[][] {
			new int[] { Directions.WEST , Directions.EAST , Directions.NORTH, Directions.SOUTH, Directions.DOWN, Directions.UP },
			new int[] { Directions.NORTH, Directions.SOUTH, Directions.EAST , Directions.WEST , Directions.DOWN, Directions.UP },
			new int[] { Directions.EAST , Directions.WEST , Directions.SOUTH, Directions.NORTH, Directions.DOWN, Directions.UP },
			new int[] { Directions.SOUTH, Directions.NORTH, Directions.WEST , Directions.EAST , Directions.DOWN, Directions.UP }
			
		},
		new int[][] {
			new int[] { Directions.WEST , Directions.EAST , Directions.NORTH, Directions.SOUTH },
			new int[] { Directions.NORTH, Directions.SOUTH, Directions.EAST , Directions.WEST  },
			new int[] { Directions.EAST , Directions.WEST , Directions.SOUTH, Directions.NORTH },
			new int[] { Directions.SOUTH, Directions.NORTH, Directions.WEST , Directions.EAST  }
		}
	) {

		@Override
		public void queueNeighbors(WireHandler handler, WireNode source, int forward) {
			/*
			 * This iteration order is designed to be an extension of the Vanilla shape
			 * update order, and is determined as follows:
			 * <br>
			 * 1. Each neighbor is identified by the step(s) you must take, starting at the
			 * source, to reach it. Each step is 1 block, thus the position of a neighbor is
			 * encoded by the direction(s) of the step(s), e.g. (right), (down), (up, left),
			 * etc.
			 * <br>
			 * 2. Neighbors are iterated over in pairs that lie on opposite sides of the
			 * source.
			 * <br>
			 * 3. Neighbors are iterated over in order of their distance from the source,
			 * moving outward. This means they are iterated over in 3 groups: direct
			 * neighbors first, then diagonal neighbors, and last are the far neighbors that
			 * are 2 blocks directly out.
			 * <br>
			 * 4. The order within each group is determined using the following basic order:
			 * { front, back, right, left, down, up }. This order was chosen because it
			 * converts to the following order of absolute directions when west is said to
			 * be 'forward': { west, east, north, south, down, up } - this is the order of
			 * shape updates.
			 */

			int rightward = (forward + 1) & 0b11;
			int backward  = (forward + 2) & 0b11;
			int leftward  = (forward + 3) & 0b11;
			int downward  = Directions.DOWN;
			int upward    = Directions.UP;

			Node front = handler.getNeighbor(source, forward);
			Node right = handler.getNeighbor(source, rightward);
			Node back  = handler.getNeighbor(source, backward);
			Node left  = handler.getNeighbor(source, leftward);
			Node below = handler.getNeighbor(source, downward);
			Node above = handler.getNeighbor(source, upward);

			// direct neighbors (6)
			handler.queueNeighbor(front, source);
			handler.queueNeighbor(back, source);
			handler.queueNeighbor(right, source);
			handler.queueNeighbor(left, source);
			handler.queueNeighbor(below, source);
			handler.queueNeighbor(above, source);

			// diagonal neighbors (12)
			handler.queueNeighbor(handler.getNeighbor(front, rightward), source);
			handler.queueNeighbor(handler.getNeighbor(back, leftward), source);
			handler.queueNeighbor(handler.getNeighbor(front, leftward), source);
			handler.queueNeighbor(handler.getNeighbor(back, rightward), source);
			handler.queueNeighbor(handler.getNeighbor(front, downward), source);
			handler.queueNeighbor(handler.getNeighbor(back, upward), source);
			handler.queueNeighbor(handler.getNeighbor(front, upward), source);
			handler.queueNeighbor(handler.getNeighbor(back, downward), source);
			handler.queueNeighbor(handler.getNeighbor(right, downward), source);
			handler.queueNeighbor(handler.getNeighbor(left, upward), source);
			handler.queueNeighbor(handler.getNeighbor(right, upward), source);
			handler.queueNeighbor(handler.getNeighbor(left, downward), source);

			// far neighbors (6)
			handler.queueNeighbor(handler.getNeighbor(front, forward), source);
			handler.queueNeighbor(handler.getNeighbor(back, backward), source);
			handler.queueNeighbor(handler.getNeighbor(right, rightward), source);
			handler.queueNeighbor(handler.getNeighbor(left, leftward), source);
			handler.queueNeighbor(handler.getNeighbor(below, downward), source);
			handler.queueNeighbor(handler.getNeighbor(above, upward), source);
		}
	},
	HORIZONTAL_FIRST_INWARD(
		new int[][] {
			new int[] { Directions.WEST , Directions.EAST , Directions.NORTH, Directions.SOUTH, Directions.DOWN, Directions.UP },
			new int[] { Directions.NORTH, Directions.SOUTH, Directions.EAST , Directions.WEST , Directions.DOWN, Directions.UP },
			new int[] { Directions.EAST , Directions.WEST , Directions.SOUTH, Directions.NORTH, Directions.DOWN, Directions.UP },
			new int[] { Directions.SOUTH, Directions.NORTH, Directions.WEST , Directions.EAST , Directions.DOWN, Directions.UP }
		},
		new int[][] {
			new int[] { Directions.WEST , Directions.EAST , Directions.NORTH, Directions.SOUTH },
			new int[] { Directions.NORTH, Directions.SOUTH, Directions.EAST , Directions.WEST  },
			new int[] { Directions.EAST , Directions.WEST , Directions.SOUTH, Directions.NORTH },
			new int[] { Directions.SOUTH, Directions.NORTH, Directions.WEST , Directions.EAST  }
		}
	) {

		@Override
		public void queueNeighbors(WireHandler handler, WireNode source, int forward) {
			/*
			 * This iteration order is designed to be an inversion of the above update
			 * order, and is determined as follows:
			 * <br>
			 * 1. Each neighbor is identified by the step(s) you must take, starting at the
			 * source, to reach it. Each step is 1 block, thus the position of a neighbor is
			 * encoded by the direction(s) of the step(s), e.g. (right), (down), (up, left),
			 * etc.
			 * <br>
			 * 2. Neighbors are iterated over in pairs that lie on opposite sides of the
			 * source.
			 * <br>
			 * 3. Neighbors are iterated over in order of their distance from the source,
			 * moving inward. This means they are iterated over in 3 groups: neighbors that
			 * are 2 blocks directly out first, then diagonal neighbors, and last are direct
			 * neighbors.
			 * <br>
			 * 4. The order within each group is determined using the following basic order:
			 * { front, back, right, left, down, up }. This order was chosen because it
			 * converts to the following order of absolute directions when west is said to
			 * be 'forward': { west, east, north, south, down, up } - this is the order of
			 * shape updates.
			 */

			int rightward = (forward + 1) & 0b11;
			int backward  = (forward + 2) & 0b11;
			int leftward  = (forward + 3) & 0b11;
			int downward  = Directions.DOWN;
			int upward    = Directions.UP;

			Node front = handler.getNeighbor(source, forward);
			Node right = handler.getNeighbor(source, rightward);
			Node back  = handler.getNeighbor(source, backward);
			Node left  = handler.getNeighbor(source, leftward);
			Node below = handler.getNeighbor(source, downward);
			Node above = handler.getNeighbor(source, upward);

			// far neighbors (6)
			handler.queueNeighbor(handler.getNeighbor(front, forward), source);
			handler.queueNeighbor(handler.getNeighbor(back, backward), source);
			handler.queueNeighbor(handler.getNeighbor(right, rightward), source);
			handler.queueNeighbor(handler.getNeighbor(left, leftward), source);
			handler.queueNeighbor(handler.getNeighbor(below, downward), source);
			handler.queueNeighbor(handler.getNeighbor(above, upward), source);

			// diagonal neighbors (12)
			handler.queueNeighbor(handler.getNeighbor(front, rightward), source);
			handler.queueNeighbor(handler.getNeighbor(back, leftward), source);
			handler.queueNeighbor(handler.getNeighbor(front, leftward), source);
			handler.queueNeighbor(handler.getNeighbor(back, rightward), source);
			handler.queueNeighbor(handler.getNeighbor(front, downward), source);
			handler.queueNeighbor(handler.getNeighbor(back, upward), source);
			handler.queueNeighbor(handler.getNeighbor(front, upward), source);
			handler.queueNeighbor(handler.getNeighbor(back, downward), source);
			handler.queueNeighbor(handler.getNeighbor(right, downward), source);
			handler.queueNeighbor(handler.getNeighbor(left, upward), source);
			handler.queueNeighbor(handler.getNeighbor(right, upward), source);
			handler.queueNeighbor(handler.getNeighbor(left, downward), source);

			
			// direct neighbors (6)
			handler.queueNeighbor(front, source);
			handler.queueNeighbor(back, source);
			handler.queueNeighbor(right, source);
			handler.queueNeighbor(left, source);
			handler.queueNeighbor(below, source);
			handler.queueNeighbor(above, source);
		}
	},
	VERTICAL_FIRST_OUTWARD(
		new int[][] {
			new int[] { Directions.DOWN, Directions.UP, Directions.WEST , Directions.EAST , Directions.NORTH, Directions.SOUTH },
			new int[] { Directions.DOWN, Directions.UP, Directions.NORTH, Directions.SOUTH, Directions.EAST , Directions.WEST  },
			new int[] { Directions.DOWN, Directions.UP, Directions.EAST , Directions.WEST , Directions.SOUTH, Directions.NORTH },
			new int[] { Directions.DOWN, Directions.UP, Directions.SOUTH, Directions.NORTH, Directions.WEST , Directions.EAST  }
		},
		new int[][] {
			new int[] { Directions.WEST , Directions.EAST , Directions.NORTH, Directions.SOUTH },
			new int[] { Directions.NORTH, Directions.SOUTH, Directions.EAST , Directions.WEST  },
			new int[] { Directions.EAST , Directions.WEST , Directions.SOUTH, Directions.NORTH },
			new int[] { Directions.SOUTH, Directions.NORTH, Directions.WEST , Directions.EAST  }
		}
	) {

		@Override
		public void queueNeighbors(WireHandler handler, WireNode source, int forward) {
			/*
			 * This iteration order is designed to be the opposite of the Vanilla shape
			 * update order, and is determined as follows:
			 * <br>
			 * 1. Each neighbor is identified by the step(s) you must take, starting at the
			 * source, to reach it. Each step is 1 block, thus the position of a neighbor is
			 * encoded by the direction(s) of the step(s), e.g. (right), (down), (up, left),
			 * etc.
			 * <br>
			 * 2. Neighbors are iterated over in pairs that lie on opposite sides of the
			 * source.
			 * <br>
			 * 3. Neighbors are iterated over in order of their distance from the source,
			 * moving outward. This means they are iterated over in 3 groups: direct
			 * neighbors first, then diagonal neighbors, and last are the far neighbors that
			 * are 2 blocks directly out.
			 * <br>
			 * 4. The order within each group is determined using the following basic order:
			 * { down, up, front, back, right, left }. This order was chosen because it
			 * converts to the following order of absolute directions when west is said to
			 * be 'forward': { down, up west, east, north, south } - this is the order of
			 * shape updates, with the vertical directions moved to the front.
			 */

			int rightward = (forward + 1) & 0b11;
			int backward  = (forward + 2) & 0b11;
			int leftward  = (forward + 3) & 0b11;
			int downward  = Directions.DOWN;
			int upward    = Directions.UP;

			Node front = handler.getNeighbor(source, forward);
			Node right = handler.getNeighbor(source, rightward);
			Node back  = handler.getNeighbor(source, backward);
			Node left  = handler.getNeighbor(source, leftward);
			Node below = handler.getNeighbor(source, downward);
			Node above = handler.getNeighbor(source, upward);

			// direct neighbors (6)
			handler.queueNeighbor(below, source);
			handler.queueNeighbor(above, source);
			handler.queueNeighbor(front, source);
			handler.queueNeighbor(back, source);
			handler.queueNeighbor(right, source);
			handler.queueNeighbor(left, source);

			// diagonal neighbors (12)
			handler.queueNeighbor(handler.getNeighbor(below, forward), source);
			handler.queueNeighbor(handler.getNeighbor(above, backward), source);
			handler.queueNeighbor(handler.getNeighbor(below, backward), source);
			handler.queueNeighbor(handler.getNeighbor(above, forward), source);
			handler.queueNeighbor(handler.getNeighbor(below, rightward), source);
			handler.queueNeighbor(handler.getNeighbor(above, leftward), source);
			handler.queueNeighbor(handler.getNeighbor(below, leftward), source);
			handler.queueNeighbor(handler.getNeighbor(above, rightward), source);
			handler.queueNeighbor(handler.getNeighbor(front, rightward), source);
			handler.queueNeighbor(handler.getNeighbor(back, leftward), source);
			handler.queueNeighbor(handler.getNeighbor(front, leftward), source);
			handler.queueNeighbor(handler.getNeighbor(back, rightward), source);

			// far neighbors (6)
			handler.queueNeighbor(handler.getNeighbor(below, downward), source);
			handler.queueNeighbor(handler.getNeighbor(above, upward), source);
			handler.queueNeighbor(handler.getNeighbor(front, forward), source);
			handler.queueNeighbor(handler.getNeighbor(back, backward), source);
			handler.queueNeighbor(handler.getNeighbor(right, rightward), source);
			handler.queueNeighbor(handler.getNeighbor(left, leftward), source);
		}
	},
	VERTICAL_FIRST_INWARD(
		new int[][] {
			new int[] { Directions.DOWN, Directions.UP, Directions.WEST , Directions.EAST , Directions.NORTH, Directions.SOUTH },
			new int[] { Directions.DOWN, Directions.UP, Directions.NORTH, Directions.SOUTH, Directions.EAST , Directions.WEST  },
			new int[] { Directions.DOWN, Directions.UP, Directions.EAST , Directions.WEST , Directions.SOUTH, Directions.NORTH },
			new int[] { Directions.DOWN, Directions.UP, Directions.SOUTH, Directions.NORTH, Directions.WEST , Directions.EAST  }
		},
		new int[][] {
			new int[] { Directions.WEST , Directions.EAST , Directions.NORTH, Directions.SOUTH },
			new int[] { Directions.NORTH, Directions.SOUTH, Directions.EAST , Directions.WEST  },
			new int[] { Directions.EAST , Directions.WEST , Directions.SOUTH, Directions.NORTH },
			new int[] { Directions.SOUTH, Directions.NORTH, Directions.WEST , Directions.EAST  }
		}
	) {

		@Override
		public void queueNeighbors(WireHandler handler, WireNode source, int forward) {
			/*
			 * This iteration order is designed to be an inversion of the above update
			 * order, and is determined as follows:
			 * <br>
			 * 1. Each neighbor is identified by the step(s) you must take, starting at the
			 * source, to reach it. Each step is 1 block, thus the position of a neighbor is
			 * encoded by the direction(s) of the step(s), e.g. (right), (down), (up, left),
			 * etc.
			 * <br>
			 * 2. Neighbors are iterated over in pairs that lie on opposite sides of the
			 * source.
			 * <br>
			 * 3. Neighbors are iterated over in order of their distance from the source,
			 * moving inward. This means they are iterated over in 3 groups: neighbors that
			 * are 2 blocks directly out first, then diagonal neighbors, and last are direct
			 * neighbors.
			 * <br>
			 * 4. The order within each group is determined using the following basic order:
			 * { down, up, front, back, right, left }. This order was chosen because it
			 * converts to the following order of absolute directions when west is said to
			 * be 'forward': { down, up west, east, north, south } - this is the order of
			 * shape updates, with the vertical directions moved to the front.
			 */

			int rightward = (forward + 1) & 0b11;
			int backward  = (forward + 2) & 0b11;
			int leftward  = (forward + 3) & 0b11;
			int downward  = Directions.DOWN;
			int upward    = Directions.UP;

			Node front = handler.getNeighbor(source, forward);
			Node right = handler.getNeighbor(source, rightward);
			Node back  = handler.getNeighbor(source, backward);
			Node left  = handler.getNeighbor(source, leftward);
			Node below = handler.getNeighbor(source, downward);
			Node above = handler.getNeighbor(source, upward);

			// far neighbors (6)
			handler.queueNeighbor(handler.getNeighbor(below, downward), source);
			handler.queueNeighbor(handler.getNeighbor(above, upward), source);
			handler.queueNeighbor(handler.getNeighbor(front, forward), source);
			handler.queueNeighbor(handler.getNeighbor(back, backward), source);
			handler.queueNeighbor(handler.getNeighbor(right, rightward), source);
			handler.queueNeighbor(handler.getNeighbor(left, leftward), source);

			// diagonal neighbors (12)
			handler.queueNeighbor(handler.getNeighbor(below, forward), source);
			handler.queueNeighbor(handler.getNeighbor(above, backward), source);
			handler.queueNeighbor(handler.getNeighbor(below, backward), source);
			handler.queueNeighbor(handler.getNeighbor(above, forward), source);
			handler.queueNeighbor(handler.getNeighbor(below, rightward), source);
			handler.queueNeighbor(handler.getNeighbor(above, leftward), source);
			handler.queueNeighbor(handler.getNeighbor(below, leftward), source);
			handler.queueNeighbor(handler.getNeighbor(above, rightward), source);
			handler.queueNeighbor(handler.getNeighbor(front, rightward), source);
			handler.queueNeighbor(handler.getNeighbor(back, leftward), source);
			handler.queueNeighbor(handler.getNeighbor(front, leftward), source);
			handler.queueNeighbor(handler.getNeighbor(back, rightward), source);

			// direct neighbors (6)
			handler.queueNeighbor(below, source);
			handler.queueNeighbor(above, source);
			handler.queueNeighbor(front, source);
			handler.queueNeighbor(back, source);
			handler.queueNeighbor(right, source);
			handler.queueNeighbor(left, source);
		}
	};

	private final int[][] directNeighbors;
	private final int[][] cardinalNeighbors;

	UpdateOrder(int[][] directNeighbors, int[][] cardinalNeighbors) {
		this.directNeighbors = directNeighbors;
		this.cardinalNeighbors = cardinalNeighbors;
	}

	public String id() {
		return name().toLowerCase(Locale.ENGLISH);
	}

	public static UpdateOrder byId(String id) {
		return valueOf(id.toUpperCase(Locale.ENGLISH));
	}

	public int[] directNeighbors(int forward) {
		return directNeighbors[forward];
	}

	public int[] cardinalNeighbors(int forward) {
		return cardinalNeighbors[forward];
	}

	/**
	 * Iterate over all neighboring nodes of the given source node. The iteration
	 * order is built from relative directions around the source, depending on the
	 * given 'forward' direction. This is an effort to eliminate any directional
	 * biases that would be emerge in rotationally symmetric circuits if the update
	 * order was built from absolute directions around the source.
	 * <br>
	 * Each update order must include the source's direct neighbors, but further
	 * neighbors may not be included.
	 */
	public abstract void queueNeighbors(WireHandler handler, WireNode source, int forward);

}
