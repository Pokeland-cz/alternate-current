package alternate.current.wire;

import java.util.Arrays;

import alternate.current.wire.WireHandler.Directions;

public class WireConnectionManager {

	/** The owner of these connections. */
	final WireNode owner;

	/** The first connection for each cardinal direction. */
	final WireConnection[] heads;

	WireConnection head;
	WireConnection tail;

	/** The total number of connections. */
	int total;

	/**
	 * A 4 bit number that encodes which in direction(s) the owner has connections
	 * to other wires.
	 */
	private int flowTotal;
	/** The direction of flow based connections to other wires. */
	int iFlowDir;

	WireConnectionManager(WireNode owner) {
		this.owner = owner;

		this.heads = new WireConnection[Directions.HORIZONTAL.length];

		this.total = 0;

		this.flowTotal = 0;
		this.iFlowDir = -1;
	}

	void set(WireHandler handler) {
		if (total > 0) {
			clear();
		}

		boolean belowIsConductor = handler.getNeighbor(owner, Directions.DOWN).isConductor();
		boolean aboveIsConductor = handler.getNeighbor(owner, Directions.UP).isConductor();

		for (int iDir = 0; iDir < Directions.HORIZONTAL.length; iDir++) {
			Node neighbor = handler.getNeighbor(owner, iDir);

			if (neighbor.isWire()) {
				add(neighbor.asWire(), iDir, true, true);
			} else {
				boolean sideIsConductor = neighbor.isConductor();

				if (!sideIsConductor) {
					Node node = handler.getNeighbor(neighbor, Directions.DOWN);

					if (node.isWire()) {
						add(node.asWire(), iDir, belowIsConductor, true);
					}
				}
				if (!aboveIsConductor) {
					Node node = handler.getNeighbor(neighbor, Directions.UP);

					if (node.isWire()) {
						add(node.asWire(), iDir, true, sideIsConductor);
					}
				}
			}
		}

		if (total > 0) {
			iFlowDir = WireHandler.FLOW_IN_TO_FLOW_OUT[flowTotal];
		}
	}

	private void clear() {
		Arrays.fill(heads, null);

		head = null;
		tail = null;

		total = 0;

		flowTotal = 0;
		iFlowDir = -1;
	}

	private void add(WireNode wire, int iDir, boolean offer, boolean accept) {
		add(new WireConnection(wire, iDir, offer, accept));
	}

	private void add(WireConnection connection) {
		if (head == null) {
			head = connection;
			tail = connection;
		} else {
			tail.next = connection;
			tail = connection;
		}

		total++;

		if (heads[connection.iDir] == null) {
			heads[connection.iDir] = connection;
			flowTotal |= (1 << connection.iDir);
		}
	}


}
