package com.terrytec.brokenrailmonitor;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import com.terrytec.brokenrailmonitor.Enums.CommandType;
import com.terrytec.brokenrailmonitor.Enums.DataLevel;
import com.terrytec.brokenrailmonitor.classes.DensityUtil;
import com.terrytec.brokenrailmonitor.classes.FileOperate;
import com.terrytec.brokenrailmonitor.classes.FileServer;
import com.terrytec.brokenrailmonitor.classes.SendDataPackage;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
//import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class HomeFragment extends Fragment {

	private final int RECEIVED = 0;
	private final int CONNECTED = 1;
	private final int SEND = 2;
	private final int RECEIVEFILE = 3;
	private final int SENDFILE = 4;
	public List<TerminalAnd2Rails> terminalAnd2Rails;
	private View vTabHome;
	private static final String ServerIP = "103.44.145.248";
	private static final int ServerPort = 23539;
	private static final int fileReceivePort = 23955;
	private Socket socket = null;
	private Socket fileSocket = null;
	private boolean isConnect = false;
	private boolean isReceive = false;
	private Handler myHandler = null;
	private ReceiveThread receiveThread = null;
	private String strMessage;
	private OutputStream outStream;
	private CustomTitleBar ctbHomeTitle;
	private Button btnUpload;
	private Button btnDownload;
	public byte[] sendBytesBuffer;
	private Boolean isInEditMode = false;
	// 声明PopupWindow对象的引用
	private PopupWindow popupWindow;
	private LayoutInflater inflaterGlobal;
	private Boolean isSubscribingAllRailInfo = false;
	// 本页面传递时的按钮btn的标志码
	// private final static int request_Code = 11;
	private OnClickListener btnEditListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				Log.e("zhenji", "进入编辑单击处理事件");
				LinearLayout llContainer = (LinearLayout) vTabHome.findViewById(R.id.llTerminalAndRailsContainer);
				switchUpDownLoadBtnVisibility();

				if (terminalAnd2Rails != null) {
					for (TerminalAnd2Rails tAnd2Rs : terminalAnd2Rails) {
						Button btnAdd = (Button) tAnd2Rs.findViewById(R.id.btnAdd);
						Button btnDelete = (Button) tAnd2Rs.findViewById(R.id.btnDelete);
						LinearLayout llStressTempeLeft = (LinearLayout) tAnd2Rs.findViewById(R.id.llStressTempeLeft);
						LinearLayout llStressTempeRight = (LinearLayout) tAnd2Rs.findViewById(R.id.llStressTempeRight);
						if (!isInEditMode) {
							btnAdd.setVisibility(View.VISIBLE);
							btnDelete.setVisibility(View.VISIBLE);
							llStressTempeLeft.setVisibility(View.GONE);
							llStressTempeRight.setVisibility(View.GONE);
						} else {
							btnAdd.setVisibility(View.GONE);
							btnDelete.setVisibility(View.GONE);
							llStressTempeLeft.setVisibility(View.VISIBLE);
							llStressTempeRight.setVisibility(View.VISIBLE);
						}
					}
					View view = llContainer.getChildAt(llContainer.getChildCount() - 1);
					Button btnAdd = (Button) view;
					if (btnAdd != null) {
						if (btnAdd.getVisibility() == View.INVISIBLE) {
							btnAdd.setVisibility(View.VISIBLE);
						} else {
							btnAdd.setVisibility(View.INVISIBLE);
						}
					}
				} else {
					Log.e("zhenji", "终端列表为空");
					if (llContainer.getChildCount() == 0) {
						creatUniqueAddButton(llContainer, btnAddFirstListener, true);
						Log.e("zhenji", "创建按钮");
					} else {
						llContainer.removeAllViews();
						Log.e("zhenji", "清空视图");
					}
				}
				Button btnEdit = ctbHomeTitle.getTitleBarRightBtn();
				String strButtonEdit = btnEdit.getText().toString();
				if (strButtonEdit.equals("编辑")) {
					btnEdit.setText("退出");
					isInEditMode = true;
				} else if (strButtonEdit.equals("退出")) {
					btnEdit.setText("编辑");
					isInEditMode = false;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Button btnAdd = new Button(getActivity());
			// btnAdd.setText("+");
			// btnAdd.setTextSize(16);
			// btnAdd.setTextColor(0xFFFFFF);
			// btnAdd.setBackgroundResource(R.drawable.button_add);
			// btnAdd.setHeight(20);
			// btnAdd.setWidth(20);
			// btnAdd.setOnClickListener(btnAddListener);
			// llAdd.addView(btnAdd);
		}
	};

	private void switchUpDownLoadBtnVisibility() {
		if (btnUpload.getVisibility() == View.INVISIBLE) {
			btnUpload.setVisibility(View.VISIBLE);
			btnDownload.setVisibility(View.VISIBLE);
		} else {
			btnUpload.setVisibility(View.INVISIBLE);
			btnDownload.setVisibility(View.INVISIBLE);
		}
	}

	private void invisibleUpDownLoadBtn() {
		btnUpload.setVisibility(View.INVISIBLE);
		btnDownload.setVisibility(View.INVISIBLE);
	}

	private void setBtnEditText2Edit() {
		if (ctbHomeTitle != null) {
			Button btnEdit = ctbHomeTitle.getTitleBarRightBtn();
			btnEdit.setText("编辑");
			isInEditMode = false;
		}
	}

	private OnClickListener btnAddListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				// Intent是系统各组件之间进行数据传递的数据负载者，即通信使者
				Intent intent = new Intent();

				// 设置页面转向
				intent.setClass(getActivity(), EditTerminalActivity.class);
				TerminalAnd2Rails tAnd2R = (TerminalAnd2Rails) ((Button) v).getTag();
				int index = terminalAnd2Rails.indexOf(tAnd2R);
				intent.putExtra("index", String.valueOf(index));
				// 设置传递参数
				startActivity(intent);// 不需要接收返回值时使用
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private OnClickListener btnAddFirstListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				// Intent是系统各组件之间进行数据传递的数据负载者，即通信使者
				Intent intent = new Intent();
				// 设置页面转向
				intent.setClass(getActivity(), EditTerminalActivity.class);
				intent.putExtra("index", String.valueOf(-1));
				// 设置传递参数
				startActivity(intent);// 不需要接收返回值时使用
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private OnClickListener btnAddLastListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				// Intent是系统各组件之间进行数据传递的数据负载者，即通信使者
				Intent intent = new Intent();
				// 设置页面转向
				intent.setClass(getActivity(), EditTerminalActivity.class);
				intent.putExtra("index", String.valueOf(-2));
				// 设置传递参数
				startActivity(intent);// 不需要接收返回值时使用
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private OnClickListener btnDeleteListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				TerminalAnd2Rails tAnd2R = (TerminalAnd2Rails) ((Button) v).getTag();
				final int index = terminalAnd2Rails.indexOf(tAnd2R);
				int tNo = tAnd2R.terminalNo;
				// tAnd2Rs.clear();
				new AlertDialog.Builder(MainActivity.getMainActivity()).setTitle("系统提示")// 设置对话框标题
						.setMessage("确认删除" + String.valueOf(tNo) + "号终端？")// 设置显示的内容
						.setPositiveButton("确定", new DialogInterface.OnClickListener() {// 添加确定按钮
							@Override
							public void onClick(DialogInterface dialog, int which) {// 确定按钮的响应事件

								// int index =
								// terminalAnd2Rails.indexOf(tAnd2R);
								terminalAnd2Rails.remove(index);
								FileOperate fileOperator = new FileOperate((MainActivity) getActivity());
								fileOperator.write(terminalAnd2Rails);
								freshDevices();
							}
						}).setNegativeButton("返回", new DialogInterface.OnClickListener() {// 添加返回按钮
							@Override
							public void onClick(DialogInterface dialog, int which) {// 响应事件

							}
						}).show();// 在按键响应事件中显示此对话框

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	int hitCount = 0;
	private OnClickListener btnUploadListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// String path =
			// MainActivity.getMainActivity().getFilesDir().toString();
			//
			// Toast.makeText(MainActivity.getMainActivity(), path,
			// Toast.LENGTH_LONG).show();
			//
			// hitCount++;
			// if (terminalAnd2Rails.size() > 0) {
			// if (hitCount % 2 == 0) {
			// terminalAnd2Rails.get(0).changeLeftRailNormal();
			// terminalAnd2Rails.get(0).setAccessPointConnect();
			// } else {
			// terminalAnd2Rails.get(0).changeLeftRailError();
			// terminalAnd2Rails.get(0).setAccessPointNotConnect();
			// }
			// }

			if (!isConnect) {
				Toast.makeText(MainActivity.getMainActivity(), "请先连接！", Toast.LENGTH_LONG).show();
				return;
			}

			sendBytesBuffer = SendDataPackage.PackageSendData((byte) MainActivity.getMainActivity().ClientID,
					(byte) 0xff, (byte) CommandType.UploadConfig.getValue(), new byte[0]);
			new Thread(sendBytesThread).start();
			new Thread(fileUploadThread).start();
		}
	};

	private OnClickListener btnDownloadListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!isConnect) {
				Toast.makeText(MainActivity.getMainActivity(), "请先连接！", Toast.LENGTH_LONG).show();
				return;
			}
			sendBytesBuffer = SendDataPackage.PackageSendData((byte) MainActivity.getMainActivity().ClientID,
					(byte) 0xff, (byte) CommandType.RequestConfig.getValue(), new byte[] { 0x48, 0x5f });
			new Thread(sendBytesThread).start();
			new Thread(fileConnectThread).start();
		}
	};

	private OnClickListener btnConnectListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (!isConnect) {
				new Thread(connectThread).start();
			}
		}
	};

	Runnable fileConnectThread = new Runnable() {

		@Override
		public void run() {
			try {
				fileSocket = new Socket(ServerIP, fileReceivePort);
				Log.e("FILE", "----file connected success----");
				try {
					Thread.currentThread();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				new Thread(new FileServer(fileSocket)).start();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	};

	Runnable fileUploadThread = new Runnable() {

		@Override
		public void run() {

			Socket data;
			try {
				data = new Socket(ServerIP, fileReceivePort);
				OutputStream outputData = data.getOutputStream();
				FileInputStream fileInput;
				try {
					fileInput = new FileInputStream(MainActivity.getMainActivity().getFilesDir() + "/config.xml");
					int size = -1;
					byte[] buffer = new byte[1024];
					while ((size = fileInput.read(buffer, 0, 1024)) != -1) {
						outputData.write(buffer, 0, size);
					}
					outputData.close();
					fileInput.close();
					data.close();

					Message msg = new Message();
					msg.what = SENDFILE;
					myHandler.sendMessage(msg);

				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	};

	// 连接到服务器的接口
	Runnable connectThread = new Runnable() {

		@Override
		public void run() {
			try {
				// 初始化Scoket，连接到服务器
				socket = new Socket(ServerIP, ServerPort);
				isConnect = true;
				// 启动接收线程
				isReceive = true;
				receiveThread = new ReceiveThread(socket);
				receiveThread.start();
				System.out.println("----connected success----");

				Message msg = new Message();
				msg.what = CONNECTED;
				myHandler.sendMessage(msg);

				String ip = socket.getLocalAddress().toString();
				if (ip.length() > 1) {
					ip = ip.substring(1, ip.length());
				}
				strMessage = "手机" + ip + ":" + socket.getLocalPort();
				new Thread(sendThread).start();
			} catch (UnknownHostException e) {
				e.printStackTrace();
				System.out.println("UnknownHostException-->" + e.toString());
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("IOException" + e.toString());
			}
		}
	};

	@SuppressLint("HandlerLeak")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == RECEIVED || msg.what == SEND) {
					try {
						byte[] receivedBytes = (byte[]) msg.obj;
						handleData(receivedBytes);
						if (receivedBytes.length > 5) {
							if (receivedBytes[0] == 0x55 && (receivedBytes[1] & 0xFF) == 0xAA) {
								switch (CommandType.valueOf(receivedBytes[5] & 0xFF)) {
								case AssignClientID:
									MainActivity.getMainActivity().ClientID = receivedBytes[4];
									TextView tvClientID = (TextView) vTabHome.findViewById(R.id.tvClientID);
									tvClientID.setText(String.valueOf(receivedBytes[4]));
									break;
								case RequestConfig:
								case ReadPointInfo:

								default:
									break;
								}
							}
							String receiveStr = new String(receivedBytes);
							if (receiveStr.length() > 6 && receiveStr.substring(0, 3).equals("###")) {
								int terminalNo = Integer.valueOf(receiveStr.substring(3, 6));
								int index = FindMasterControlIndex(terminalNo);
								if (index != -1) {
									terminalAnd2Rails.get(index).setAccessPointConnect();
								}
							}
						} else if (receivedBytes.length == 0) {
							Toast.makeText(MainActivity.getMainActivity(), "与服务器断开连接！", Toast.LENGTH_LONG).show();
							ctbHomeTitle.getTitleBarLeftBtn().setText("连接");
							TextView tvClientID = (TextView) vTabHome.findViewById(R.id.tvClientID);
							tvClientID.setText("0");
							if (terminalAnd2Rails != null) {
								for (TerminalAnd2Rails tAnd2R : terminalAnd2Rails) {
									tAnd2R.setAccessPointNotConnect();
								}
							}
						}
						((CommandFragment) MainActivity.getMainActivity().commandFragment).AddCmdMsg(receivedBytes,
								DataLevel.Default);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (msg.what == CONNECTED) {
					ctbHomeTitle.getTitleBarLeftBtn().setText("已连接");
				} else if (msg.what == RECEIVEFILE) {
					try {
						freshDevices();
						((CommandFragment) MainActivity.getMainActivity().commandFragment)
								.AddCmdMsg("终端配置文件完成接收".getBytes(), DataLevel.Normal);
					} catch (Exception e) {
						((CommandFragment) MainActivity.getMainActivity().commandFragment)
								.AddCmdMsg("配置文件接收异常！".getBytes(), DataLevel.Error);
					}
				} else if (msg.what == SENDFILE) {
					((CommandFragment) MainActivity.getMainActivity().commandFragment)
							.AddCmdMsg("终端配置文件发送成功".getBytes(), DataLevel.Normal);
				}
			}
		};
		Log.e("zyn", this.getClass().getName() + " onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e("zyn", this.getClass().getName() + " onCreateView");
		inflaterGlobal = inflater;
		vTabHome = inflater.inflate(R.layout.tabhome, container, false);
		btnUpload = (Button) vTabHome.findViewById(R.id.btnUpload);
		btnDownload = (Button) vTabHome.findViewById(R.id.btnDownload);
		btnUpload.setOnClickListener(btnUploadListener);
		btnDownload.setOnClickListener(btnDownloadListener);
		freshDevices();
		// 点击按钮弹出菜单
		Button pop = (Button) vTabHome.findViewById(R.id.btnPop);
		pop.setOnClickListener(popClick);
		return vTabHome;
	}

	// private View.OnClickListener testClick = new View.OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	//
	// Toast.makeText(MainActivity.getMainActivity(), "test按钮被点击",
	// Toast.LENGTH_LONG).show();
	// }
	// };

	// 点击弹出左侧菜单的显示方式
	private OnClickListener popClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			getPopupWindow();
			// 这里是位置显示方式,在按钮的左下角
			// popupWindow.showAsDropDown(v);
			// 这里可以尝试其它效果方式,如popupWindow.showAsDropDown(v,
			// (screenWidth-dialgoWidth)/2, 0);
			popupWindow.showAtLocation(vTabHome, Gravity.CENTER, 0, 0);
		}
	};

	/***
	 * 获取PopupWindow实例
	 */
	private void getPopupWindow() {
		if (null != popupWindow) {
			popupWindow.dismiss();
			return;
		} else {
			initPopuptWindow();
		}
	}

	/**
	 * 创建PopupWindow
	 */
	protected void initPopuptWindow() {
		// 获取自定义布局文件pop.xml的视图
		View popupWindow_view = inflaterGlobal.inflate(R.layout.activity_popwindow, null, false);
		// 创建PopupWindow实例,200,150分别是宽度和高度
		popupWindow = new PopupWindow(popupWindow_view, 500, 300, true);
		// popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
		// popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
		// popupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.layout_popupwindow_style01,
		// null));
		// 设置动画效果
		popupWindow.setAnimationStyle(R.style.AnimationFade);
		// 点击其他地方消失
		popupWindow_view.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
					popupWindow = null;
				}
				return false;
			}
		});

		// pop.xml视图里面的控件
		final Button btnSubscribe = (Button) popupWindow_view.findViewById(R.id.btnSubscribe);
		// pop.xml视图里面的控件触发的事件
		// 打开
		btnSubscribe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					// 这里可以执行相关操作
					if (!isConnect) {
						Toast.makeText(MainActivity.getMainActivity(), "请先连接！", Toast.LENGTH_LONG).show();
						popupWindow.dismiss();
						return;
					}
					if (isSubscribingAllRailInfo) {
						isSubscribingAllRailInfo = false;
						btnSubscribe.setText(R.string.subscribe);

						sendBytesBuffer = SendDataPackage.PackageSendData(
								(byte) MainActivity.getMainActivity().ClientID, (byte) 0xff,
								(byte) CommandType.SubscribeAllRailInfo.getValue(), new byte[] { (byte) 0xff });
					} else {
						isSubscribingAllRailInfo = true;
						btnSubscribe.setText(R.string.unsubscribe);
						sendBytesBuffer = SendDataPackage.PackageSendData(
								(byte) MainActivity.getMainActivity().ClientID, (byte) 0xff,
								(byte) CommandType.SubscribeAllRailInfo.getValue(), new byte[] { 0 });
					}
					new Thread(sendBytesThread).start();
					// 对话框消失
					popupWindow.dismiss();
				} catch (Exception e) {
					e.getStackTrace();
				}
			}
		});
	}

	public void freshDevices() {
		if (terminalAnd2Rails != null) {
			for (TerminalAnd2Rails tAnd2R : terminalAnd2Rails) {
				tAnd2R.setAccessPointNotConnect();
			}
		}
		FileOperate fileOperator = new FileOperate((MainActivity) getActivity());
		terminalAnd2Rails = fileOperator.GetTerminalAnd2Rails();
		LinearLayout llContainer = (LinearLayout) vTabHome.findViewById(R.id.llTerminalAndRailsContainer);
		llContainer.removeAllViews();
		if (terminalAnd2Rails != null) {
			for (TerminalAnd2Rails tAnd2R : terminalAnd2Rails) {
				if (tAnd2R != null) {
					// 初始化宽高属性,此处单位为px
					LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.WRAP_CONTENT);
					int index = terminalAnd2Rails.indexOf(tAnd2R);
					if (index != 0) {
						lp.setMargins(0, -85, 0, 0);
					}
					if (index == terminalAnd2Rails.size() - 1) {
						tAnd2R.setRailsGone();
					}
					llContainer.addView(tAnd2R, lp);// 动态改变布局
					tAnd2R.setVisibility(View.VISIBLE);// 此处需要设置布局显示，否则会不显示

					Button btnAdd = (Button) tAnd2R.findViewById(R.id.btnAdd);
					btnAdd.setOnClickListener(btnAddListener);
					btnAdd.setTag(tAnd2R);
					Button btnDelete = (Button) tAnd2R.findViewById(R.id.btnDelete);
					btnDelete.setOnClickListener(btnDeleteListener);
					btnDelete.setTag(tAnd2R);
				}
			}
			creatUniqueAddButton(llContainer, btnAddLastListener, false);
		}
		invisibleUpDownLoadBtn();
		setBtnEditText2Edit();
	}

	private void creatUniqueAddButton(LinearLayout llContainer, OnClickListener listener, Boolean isVisible) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		Button btnAdd = new Button(MainActivity.getMainActivity());
		btnAdd.setBackgroundResource(R.drawable.button_add);
		btnAdd.setHeight(DensityUtil.dip2px(MainActivity.getMainActivity(), 18));
		btnAdd.setWidth(DensityUtil.dip2px(MainActivity.getMainActivity(), 18));
		btnAdd.setPadding(0, 0, 0, 0);
		btnAdd.setText(R.string.add);
		btnAdd.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		llContainer.addView(btnAdd, lp);// 动态改变布局
		if (isVisible)
			btnAdd.setVisibility(View.VISIBLE);// 此处需要设置布局显示，否则会不显示
		else
			btnAdd.setVisibility(View.INVISIBLE);// 此处需要设置布局显示，否则会不显示
		btnAdd.setOnClickListener(listener);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ctbHomeTitle = (CustomTitleBar) getActivity().findViewById(R.id.ctbHomeTitle);
		ctbHomeTitle.setTitleRightBtnClickListener(btnEditListener);
		ctbHomeTitle.setTitleLeftBtnClickListener(btnConnectListener);
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.e("zyn", this.getClass().getName() + " onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.e("zyn", this.getClass().getName() + " onStop");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("zyn", this.getClass().getName() + " onDestroy");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		Log.e("zyn", this.getClass().getName() + " onDestroyView");
	}

	Runnable sendThread = new Runnable() {

		@Override
		public void run() {
			byte[] sendBuffer = null;
			try {
				sendBuffer = strMessage.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			try {
				outStream = socket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				outStream.write(sendBuffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	public Runnable sendBytesThread = new Runnable() {

		@Override
		public void run() {
			try {
				outStream = socket.getOutputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				outStream.write(sendBytesBuffer);

				Message msg = new Message();
				msg.what = SEND;
				msg.obj = sendBytesBuffer;
				myHandler.sendMessage(msg);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	// 接收线程
	private class ReceiveThread extends Thread {
		private InputStream inStream = null;
		private int accumulateNumber = 0;
		private byte[] RememberBuffer;
		private Boolean canMerge = false;
		private byte[] buffer;
		// private String str = null;

		ReceiveThread(Socket socket) {
			try {
				inStream = socket.getInputStream();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void run() {
			int count = 0;
			while (isReceive) {
				buffer = new byte[1024];
				try {
					count = inStream.read(buffer);

					if (Recognize1024(count)) {
						RememberRecv(count);
						canMerge = true;
						Log.e("提示", "累积收到1024字节");
						continue;
					}
					if (canMerge) {
						MergeBuffer(count);
						canMerge = false;
						count = buffer.length;
						Log.e("提示", "发生合并");
					}

					Message msg = new Message();
					msg.what = RECEIVED;
					msg.obj = getActualReceivedBytes(buffer, count);
					myHandler.sendMessage(msg);
					if (count == -1) {
						isConnect = false;
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private Boolean Recognize1024(int lengthOnce) {
			// V519发满1024字节之后会截断一下，在下一个1024字节继续发送
			// 上一句不对，满1024字节截断可能是因为花生壳，因为不通过V519，只通过服务器和手机通信也会有这种情况。
			// long beforePlusRemainder = accumulateNumber % 1024;
			accumulateNumber += lengthOnce;
			int afterPlusRemainder = accumulateNumber % 1024;
			if (afterPlusRemainder == 0) {
				// 等于0的时候说明接收的字段跨过1024字节，再收一组数据。
				// 有一种特殊情况，就是收到1024字节的时候正好是一整包，这样进入判断的话就会将两个本来就应该分开的包连起来，这种情况没有处理。
				return true;
			}
			return false;
		}

		public void RememberRecv(int length) {
			RememberBuffer = new byte[length];
			System.arraycopy(buffer, 0, RememberBuffer, 0, length);
		}

		public void MergeBuffer(int length2Merge) {
			accumulateNumber = 0;
			accumulateNumber += length2Merge;
			byte[] secondReceive = new byte[length2Merge];
			for (int i = 0; i < length2Merge; i++) {
				secondReceive[i] = this.buffer[i];
			}
			byte[] sumReceive = new byte[RememberBuffer.length + length2Merge];
			System.arraycopy(RememberBuffer, 0, sumReceive, 0, RememberBuffer.length);
			System.arraycopy(secondReceive, 0, sumReceive, RememberBuffer.length, secondReceive.length);
			buffer = new byte[sumReceive.length];
			System.arraycopy(sumReceive, 0, buffer, 0, sumReceive.length);
			RememberBuffer = null;
		}
	}

	private byte[] getActualReceivedBytes(byte[] buffer, int length) {
		if (length == -1) {
			return new byte[0];
		}
		byte[] result = new byte[length];
		System.arraycopy(buffer, 0, result, 0, length);
		return result;
	}

	public Handler getHomeHandler() {
		return myHandler;
	}

	public int FindMasterControlIndex(int terminalNo) {
		int i = 0;
		for (TerminalAnd2Rails tAnd2R : terminalAnd2Rails) {
			if (tAnd2R.terminalNo == terminalNo) {
				return i;
			}
			i++;
		}
		return -1;
	}

	private void handleData(byte[] data) {
		if (data.length > 1 && data[0] == 0x66 && (data[1] & 0xFF) == 0xcc) {
			if (checksumCalc(data)) {

			}else
				((CommandFragment) MainActivity.getMainActivity().commandFragment)
				.AddCmdMsg("校验和出错".getBytes(), DataLevel.Error);
				
		}
	}

	private Boolean checksumCalc(byte[] data) {
		int checksum = 0;
		int length = data.length;
		for (int i = 0; i < length - 2; i++) {
			checksum += data[i];
		}
		return ((data[length - 2] & 0xFF) == ((checksum & 0xFF00) >> 8))
				&& ((data[length - 1] & 0xFF) == (checksum & 0xFF)) ? true : false;
	}

	public Boolean getIsConnect() {
		return isConnect;
	}

	public Boolean getIsInEditMode() {
		return isInEditMode;
	}

	public View getViewTabHome() {
		return vTabHome;
	}
}
