package com.ggi.uparty.network;

import java.util.ArrayList;
import java.util.Date;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class Network {

	static public void register (EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.register(SignUp.class);
		kryo.register(Account.class);
		kryo.register(Friend.class);
		kryo.register(Group.class);
		kryo.register(ArrayList.class);
		kryo.register(Login.class);
		kryo.register(ErrorMessage.class);
		kryo.register(Confirm.class);
		kryo.register(Resend.class);
		kryo.register(Event.class);
		kryo.register(UpVote.class);
		kryo.register(DownVote.class);
		kryo.register(Refresh.class);
		kryo.register(Date.class);
		kryo.register(Report.class);
		kryo.register(NewGroup.class);
		kryo.register(Member.class);
		kryo.register(Invite.class);
		kryo.register(LeaveGroup.class);
		kryo.register(DeleteGroup.class);
		kryo.register(Forgot.class);
	}
	
}
