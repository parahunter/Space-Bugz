using UnityEngine;
using System.Collections;

public interface TcpEventSubscriber
{
	void Recieve(byte id, byte[] packet, int lengthOfPacket);
}
