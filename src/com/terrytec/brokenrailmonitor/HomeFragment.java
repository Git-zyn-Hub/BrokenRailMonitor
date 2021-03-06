package com.terrytec.brokenrailmonitor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.List;

import com.terrytec.brokenrailmonitor.Enums.CommandType;
import com.terrytec.brokenrailmonitor.Enums.DataLevel;
import com.terrytec.brokenrailmonitor.classes.DensityUtil;
import com.terrytec.brokenrailmonitor.classes.FileOperate;
import com.terrytec.brokenrailmonitor.classes.FileRailInfoXmlParser;
import com.terrytec.brokenrailmonitor.classes.FileServer;
import com.terrytec.brokenrailmonitor.classes.MacAddress;
import com.terrytec.brokenrailmonitor.classes.SendDataPackage;
import com.terrytec.brokenrailmonitor.windows.ConfigInitInfoWindow;
import com.terrytec.brokenrailmonitor.windows.PasswordWindow;
import com.terrytec.brokenrailmonitor.windows.PointConfigInfoWindow;

import android.annotation.SuppressLint;
import android.app.Activity;
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
//import android.widget.Toast;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment {

	private final int RECEIVED = 0;
	private final int CONNECTED = 1;
	private final int SEND = 2;
	private final int RECEIVEFILE = 3;
	private final int SENDFILE = 4;
	private final int IOException = 5;
	private final int UnknownHostException = 6;
	public List<TerminalAnd2Rails> terminalAnd2Rails;
	private View vTabHome;
	private static String serverIP = "103.44.145.248";
	private static final String host = "f1880f0253.51mypc.cn";
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
	public Activity CurrentActivity = null;
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
				fileSocket = new Socket(serverIP, fileReceivePort);
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
				data = new Socket(serverIP, fileReceivePort);
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
				socket = new Socket(serverIP, ServerPort);
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
				strMessage = "手机" + ip + ":" + socket.getLocalPort() + "#" + MacAddress.getLocalMacAddressFromIp();
				new Thread(sendThread).start();
			} catch (UnknownHostException e) {
				e.printStackTrace();

				Message msg = new Message();
				msg.what = UnknownHostException;
				myHandler.sendMessage(msg);

				System.out.println("UnknownHostException-->" + e.toString());
			} catch (IOException e) {
				e.printStackTrace();

				Message msg = new Message();
				msg.what = IOException;
				myHandler.sendMessage(msg);

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
				switch (msg.what) {
				case RECEIVED:
				case SEND: {
					try {
						byte[] receivedBytes = (byte[]) msg.obj;
						((CommandFragment) MainActivity.getMainActivity().commandFragment).AddCmdMsg(receivedBytes,
								DataLevel.Default);
						if (receivedBytes.length > 5) {
							if (receivedBytes[0] == 0x55 && (receivedBytes[1] & 0xFF) == 0xAA) {
								switch (CommandType.valueOf(receivedBytes[5] & 0xFF)) {
								case AssignClientID:
									MainActivity.getMainActivity().ClientID = receivedBytes[4];
									TextView tvClientID = (TextView) vTabHome.findViewById(R.id.tvClientID);
									tvClientID.setText(String.valueOf(receivedBytes[4]));
									break;
								case BroadcastConfigFileSize:
									handleBroadcastFileSize(receivedBytes);
									break;
								case RequestConfig:
								case ReadPointInfo:
								default:
									break;
								}
							} else if (receivedBytes[0] == 0x66 && (receivedBytes[1] & 0xFF) == 0xcc) {
								if (checksumCalc(receivedBytes)) {
									switch (CommandType.valueOf(receivedBytes[6] & 0xFF)) {
									case GetPointRailInfo:
										handlePointRailInfoData(receivedBytes);
										break;
									case ConfigInitialInfoPassword:
										handleConfigInitialInfoPassword(receivedBytes);
										break;
									case ReadPointInfo:
										handleReadPointInfo(receivedBytes);
										break;
									default:
										break;
									}
								} else
									((CommandFragment) MainActivity.getMainActivity().commandFragment)
											.AddCmdMsg("校验和出错".getBytes(), DataLevel.Error);
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
							freshDevices();
							// if (terminalAnd2Rails != null) {
							// for (TerminalAnd2Rails tAnd2R :
							// terminalAnd2Rails) {
							// tAnd2R.setAccessPointNotConnect();
							// }
							// }
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
					break;
				case CONNECTED: {
					ctbHomeTitle.getTitleBarLeftBtn().setText("已连接");
				}
					break;
				case RECEIVEFILE: {
					try {
						freshDevices();
						((CommandFragment) MainActivity.getMainActivity().commandFragment)
								.AddCmdMsg("终端配置文件完成接收".getBytes(), DataLevel.Normal);
					} catch (Exception e) {
						((CommandFragment) MainActivity.getMainActivity().commandFragment)
								.AddCmdMsg("配置文件接收异常！".getBytes(), DataLevel.Error);
					}
				}
					break;
				case SENDFILE: {
					((CommandFragment) MainActivity.getMainActivity().commandFragment)
							.AddCmdMsg("终端配置文件发送成功".getBytes(), DataLevel.Normal);
				}
					break;
				case UnknownHostException:
					Toast.makeText(MainActivity.getMainActivity(), "连接时出现未知主机异常，无法连接！", Toast.LENGTH_LONG).show();
					break;
				case IOException:
					Toast.makeText(MainActivity.getMainActivity(), "连接异常，无法连接！", Toast.LENGTH_LONG).show();
					break;

				default:
					break;
				}
			}
		};
		Log.e("zyn", this.getClass().getName() + " onCreate");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e("zyn", this.getClass().getName() + " onCreateView");
		try {
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
			new Thread(getInetAddressThread).start();
			return vTabHome;
		} catch (Exception e) {
			Toast.makeText(MainActivity.getMainActivity(), "主页创建异常！", Toast.LENGTH_LONG).show();
			return null;
		}
	}

	public static String GetInetAddress(String host) {
		String IPAddress = "";
		InetAddress ReturnStr1 = null;
		try {
			ReturnStr1 = InetAddress.getByName(host);
			IPAddress = ReturnStr1.getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Toast.makeText(MainActivity.getMainActivity(), "获取服务器IP地址，出现未知主机异常，可能无法连接！", Toast.LENGTH_LONG).show();
			return IPAddress;
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(MainActivity.getMainActivity(), "获取服务器IP地址异常，可能无法连接！", Toast.LENGTH_LONG).show();
			return IPAddress;
		}
		return IPAddress;
	}

	/**
	 * 网络操作相关的子线程
	 */
	Runnable getInetAddressThread = new Runnable() {

		@Override
		public void run() {
			serverIP = GetInetAddress(host);
		}
	};
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
		if (isSubscribingAllRailInfo)
			btnSubscribe.setText(R.string.unsubscribe);
		else
			btnSubscribe.setText(R.string.subscribe);

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

		final Button btnTest = (Button) popupWindow_view.findViewById(R.id.btnTest);
		btnTest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FileRailInfoXmlParser xmlParser = new FileRailInfoXmlParser(1);
				Calendar calendar = Calendar.getInstance();
				calendar.set(2018, 1, 20);
				xmlParser.setDateAndPath(calendar);
				xmlParser.createXml();
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

	private void handlePointRailInfoData(byte[] data) {
		if (terminalAnd2Rails != null) {
			int length = (data[2] << 8) + data[3];
			byte[] content = new byte[length - 9];
			byte[] bytesTemp = new byte[length - 9];
			int contentLength = content.length;
			for (int i = 7; i < length - 2; i++) {
				bytesTemp[i - 7] = data[i];
			}
			for (int i = 0; i < contentLength; i += 10) {
				for (int j = 0; j < 10; j++) {
					content[i + j] = bytesTemp[contentLength - i - (10 - j)];
				}
			}
			if (contentLength % 10 == 0) {
				if (contentLength == 10) {
					int index = FindMasterControlIndex(content[0]);
					if (index != -1) {
						// 检查1号左侧铁轨
						if (index != 0) {
							// 第一个终端没有上边的铁轨
							int onOffRailLeftUp = content[1] & 0x0f;
							setRailLeftState(index - 1, onOffRailLeftUp);
						}
						if (index != terminalAnd2Rails.size() - 1) {
							// 最后一个终端没有下边的铁轨
							int onOffRailLeftDown = (content[1] & 0xf0) >> 4;
							setRailLeftState(index, onOffRailLeftDown);
						}
						// 检查2号铁轨
						if (index != 0) {
							// 第一个终端没有左边的铁轨
							int onOffRailRightUp = content[2] & 0x0f;
							setRailRightState(index - 1, onOffRailRightUp);
						}
						if (index != terminalAnd2Rails.size() - 1) {
							// 最后一个终端没有右边的铁轨
							int onOffRailRightDown = (content[2] & 0xf0) >> 4;
							setRailRightState(index, onOffRailRightDown);
						}
					} else
						((CommandFragment) MainActivity.getMainActivity().commandFragment)
								.AddCmdMsg("未找到数据中包含终端号所示终端".getBytes(), DataLevel.Error);
					terminalAnd2Rails.get(index).setRailStressLeft(((content[3] & 0xff) << 8) + (content[4] & 0xff));
					terminalAnd2Rails.get(index).setRailStressRight(((content[5] & 0xff) << 8) + (content[6] & 0xff));
					terminalAnd2Rails.get(index).setRailTemperatureLeft(setMasterCtrlTemperature(content[7]));
					terminalAnd2Rails.get(index).setRailTemperatureRight(setMasterCtrlTemperature(content[8]));
					terminalAnd2Rails.get(index).setMCTemperature(setMasterCtrlTemperature(content[9]));

				} else {
					// 如果有多个终端的数据，需要处理冲突。
					for (int i = 0; i < contentLength - 10; i += 10) {
						int index = FindMasterControlIndex(content[i]);

						// 检查1号铁轨
						if (i == 0 && index != 0) {
							// 第一个终端没有左边的铁轨
							int onOffRailLeftUp = content[1] & 0x0f;
							setRailLeftState(index - 1, onOffRailLeftUp);
						} else {
							if (((content[i + 1] & 0xf0) >> 4) == (content[i + 11] & 0x0f)) {
								// 不冲突
								int onOff = (content[i + 1] & 0xf0) >> 4;
								setRailLeftState(index, onOff);
							} else if (((content[i + 1] & 0xf0) >> 4) == 9 || (content[i + 11] & 0x0f) == 9) {
								setRailLeftState(index, 9);
							} else {
								// 冲突
								setRailLeftDifferent(index);
								int tNo = terminalAnd2Rails.get(index).terminalNo;
								int tNextNo = terminalAnd2Rails.get(index + 1).terminalNo;
								String errorTerminal = "";
								if ((content[i + 1] & 0xf0) == 0x70) {
									errorTerminal = String.valueOf(tNo) + "号终端接收异常";
								} else if ((content[i + 11] & 0x0f) == 0x07) {
									errorTerminal = String.valueOf(tNextNo) + "号终端接收异常";
								}
								((CommandFragment) MainActivity.getMainActivity().commandFragment)
										.AddCmdMsg(
												(String.valueOf(tNo) + "号终端与" + String.valueOf(tNextNo)
														+ "号终端之间的1号铁轨通断信息矛盾！" + errorTerminal + "，请检查").getBytes(),
												DataLevel.Warning);

							}
						}
						if (i == (contentLength - 20)) {
							int indexLastTerminal = FindMasterControlIndex(content[i + 10]);
							if (indexLastTerminal != terminalAnd2Rails.size() - 1) {
								// 最后一个终端没有右边的铁轨
								int onOffRailLeftDown = (content[i + 11] & 0xf0) >> 4;
								setRailLeftState(indexLastTerminal, onOffRailLeftDown);
							}
						}

						// 检查2号铁轨
						if (i == 0 && index != 0) {
							// 第一个终端没有左边的铁轨
							int onOffRailRightUp = content[2] & 0x0f;
							setRailRightState(index - 1, onOffRailRightUp);
						} else {
							if (((content[i + 2] & 0xf0) >> 4) == (content[i + 12] & 0x0f)) {
								// 不冲突
								int onOff = (content[i + 2] & 0xf0) >> 4;
								setRailRightState(index, onOff);
							} else if (((content[i + 2] & 0xf0) >> 4) == 9 || (content[i + 12] & 0x0f) == 9) {
								setRailRightState(index, 9);
							} else {
								// 冲突
								setRailRightDifferent(index);
								int tNo = terminalAnd2Rails.get(index).terminalNo;
								int tNextNo = terminalAnd2Rails.get(index + 1).terminalNo;
								String errorTerminal = "";
								if ((content[i + 2] & 0xf0) == 0x70) {
									errorTerminal = String.valueOf(tNo) + "号终端接收异常";
								} else if ((content[i + 12] & 0x0f) == 0x07) {
									errorTerminal = String.valueOf(tNextNo) + "号终端接收异常";
								}
								((CommandFragment) MainActivity.getMainActivity().commandFragment)
										.AddCmdMsg(
												(String.valueOf(tNo) + "号终端与" + String.valueOf(tNextNo)
														+ "号终端之间的2号铁轨通断信息矛盾！" + errorTerminal + "，请检查").getBytes(),
												DataLevel.Warning);
							}
						}
						if (i == (contentLength - 20)) {
							int indexLastTerminal = FindMasterControlIndex(content[i + 10]);
							if (indexLastTerminal != terminalAnd2Rails.size() - 1) {
								// 最后一个终端没有右边的铁轨
								int onOffRail2Right = (content[i + 12] & 0xf0) >> 4;
								setRailRightState(indexLastTerminal, onOffRail2Right);
							}
						}
						terminalAnd2Rails.get(index)
								.setRailStressLeft(((content[i + 3] & 0xff) << 8) + (content[i + 4] & 0xff));
						terminalAnd2Rails.get(index)
								.setRailStressRight(((content[i + 5] & 0xff) << 8) + (content[i + 6] & 0xff));
						terminalAnd2Rails.get(index).setRailTemperatureLeft(setMasterCtrlTemperature(content[i + 7]));
						terminalAnd2Rails.get(index).setRailTemperatureRight(setMasterCtrlTemperature(content[i + 8]));
						terminalAnd2Rails.get(index).setMCTemperature(setMasterCtrlTemperature(content[i + 9]));
						if (i == (contentLength - 20)) {
							index = FindMasterControlIndex(content[i + 10]);
							terminalAnd2Rails.get(index)
									.setRailStressLeft(((content[i + 13] & 0xff) << 8) + (content[i + 14] & 0xff));
							terminalAnd2Rails.get(index)
									.setRailStressRight(((content[i + 15] & 0xff) << 8) + (content[i + 16] & 0xff));
							terminalAnd2Rails.get(index)
									.setRailTemperatureLeft(setMasterCtrlTemperature(content[i + 17]));
							terminalAnd2Rails.get(index)
									.setRailTemperatureRight(setMasterCtrlTemperature(content[i + 18]));
							terminalAnd2Rails.get(index).setMCTemperature(setMasterCtrlTemperature(content[i + 19]));
						}
					}
				}
			} else
				((CommandFragment) MainActivity.getMainActivity().commandFragment)
						.AddCmdMsg("发送数据内容的长度错误，应该是10的倍数".getBytes(), DataLevel.Error);

		} else
			((CommandFragment) MainActivity.getMainActivity().commandFragment).AddCmdMsg("终端及铁轨为空".getBytes(),
					DataLevel.Error);
	}

	private void handleConfigInitialInfoPassword(byte[] data) {
		TerminalCmdActivity currentActivity = (TerminalCmdActivity) CurrentActivity;
		if (currentActivity != null) {
			PasswordWindow pwdWindow = currentActivity.getPwdWindow();
			if ((data[7] & 0xff) == 0xff) {
				Toast.makeText(MainActivity.getMainActivity(), "密码错误", Toast.LENGTH_LONG).show();
				pwdWindow.ClearPwdEditText();
			} else if (data[7] == 0) {
				pwdWindow.dismiss();

				ConfigInitInfoWindow ciInfoWindow = new ConfigInitInfoWindow();
				ciInfoWindow.setLayoutInflater(inflaterGlobal);
				if (CurrentActivity != null) {
					TerminalCmdActivity tca = (TerminalCmdActivity) CurrentActivity;
					ciInfoWindow.setTerminalNo(tca.getTerminalNo());
					ciInfoWindow = ciInfoWindow.getConfigInitInfoWindow();
					ciInfoWindow.showAtLocation(CurrentActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
				}
			}
		}
	}

	///读取单点配置信息。
	private void handleReadPointInfo(byte[] data) {
		int terminalNo = data[7];
		int count = terminalAnd2Rails.size();

		int index = FindMasterControlIndex(terminalNo);
		if (index != -1) {
			if (index == 0 || index == 1) {
				if (0 != data[8]) {
					AppendMessage(terminalNo + "号终端次级相邻小终端不为0！终端没有次级相邻小终端应填0", DataLevel.Error);
				}
			} else {
				if (terminalAnd2Rails.get(index - 1).neighbourSmall != data[8]) {
					AppendMessage(
							terminalNo + "号终端次级相邻小终端不匹配！config.xml配置文件中为"
									+ terminalAnd2Rails.get(index - 1).neighbourSmall + "收到的为" + data[8],
							DataLevel.Error);
				}
			}
			if (terminalAnd2Rails.get(index).neighbourSmall != data[9]) {
				AppendMessage(terminalNo + "号终端相邻小终端不匹配！config.xml配置文件中为" + terminalAnd2Rails.get(index).neighbourSmall
						+ "收到的为" + data[9], DataLevel.Error);
			}
			if (terminalAnd2Rails.get(index).neighbourBig != data[10]) {
				AppendMessage(terminalNo + "号终端相邻大终端不匹配！config.xml配置文件中为" + terminalAnd2Rails.get(index).neighbourBig
						+ "收到的为" + data[10], DataLevel.Error);
			}
			if (index == count - 2 || index == count - 1) {
				if (0xff != data[11]) {
					AppendMessage(terminalNo + "号终端次级相邻大终端不为255！终端没有次级相邻大终端应填255", DataLevel.Error);
				}
			} else {
				if (terminalAnd2Rails.get(index + 1).neighbourBig != data[11]) {
					AppendMessage(
							terminalNo + "号终端次级相邻大终端不匹配！config.xml配置文件中为"
									+ terminalAnd2Rails.get(index + 1).neighbourBig + "收到的为" + data[11],
							DataLevel.Error);
				}
			}

			boolean flashIsValid = false;
			if (data[12] == 1) {
				flashIsValid = true;
			} else if (data[12] == 0) {
				flashIsValid = false;
			} else {
				AppendMessage("‘Flash是否有效’字段收到未定义数据。按照无效处理！", DataLevel.Error);
			}
			PointConfigInfoWindow pConfigInfoWindow = new PointConfigInfoWindow(terminalNo, data[8], data[9], data[10],
					data[11], flashIsValid);
			pConfigInfoWindow.setLayoutInflater(inflaterGlobal);

			if (CurrentActivity != null) {
				pConfigInfoWindow = pConfigInfoWindow.getPointConfigInfoWindow();
				pConfigInfoWindow.showAtLocation(CurrentActivity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
			}
		} else
			AppendMessage(terminalNo + "号终端不存在", DataLevel.Error);

	}

	private int setMasterCtrlTemperature(byte tempe) {
		int destTempe;
		int sign = (tempe & 0x80) >> 7;
		if (sign == 1) {
			destTempe = -(tempe & 0x7f);
		} else {
			destTempe = tempe & 0xff;
		}
		return destTempe;
	}

	private Boolean checksumCalc(byte[] data) {
		int checksum = 0;
		int length = data.length;
		for (int i = 0; i < length - 2; i++) {
			checksum += (data[i] & 0xFF);
		}
		return ((data[length - 2] & 0xFF) == ((checksum & 0xFF00) >> 8))
				&& ((data[length - 1] & 0xFF) == (checksum & 0xFF)) ? true : false;
	}

	private void setRailLeftState(int index, int onOff) {
		TerminalAnd2Rails tAnd2R = terminalAnd2Rails.get(index);
		if (onOff == 0) {// 通的
			tAnd2R.changeLeftRailNormal();
		} else if (onOff == 7) {// 断的
			int tNo = tAnd2R.terminalNo;
			int tNextNo = terminalAnd2Rails.get(index + 1).terminalNo;

			((CommandFragment) MainActivity.getMainActivity().commandFragment).AddCmdMsg(
					(String.valueOf(tNo) + "号终端与" + String.valueOf(tNextNo) + "号终端之间的1号铁轨断开！").getBytes(),
					DataLevel.Error);
			tAnd2R.changeLeftRailError();
		} else if (onOff == 9) {// 超时
			int tNo = tAnd2R.terminalNo;
			int tNextNo = terminalAnd2Rails.get(index + 1).terminalNo;

			((CommandFragment) MainActivity.getMainActivity().commandFragment).AddCmdMsg(
					(String.valueOf(tNo) + "号终端与" + String.valueOf(tNextNo) + "号终端之间的1号铁轨超时！").getBytes(),
					DataLevel.Timeout);
			tAnd2R.changeLeftRailTimeout();
		} else if (onOff == 0x0a) {// 持续干扰
			int tNo = tAnd2R.terminalNo;
			int tNextNo = terminalAnd2Rails.get(index + 1).terminalNo;

			((CommandFragment) MainActivity.getMainActivity().commandFragment).AddCmdMsg(
					(String.valueOf(tNo) + "号终端与" + String.valueOf(tNextNo) + "号终端之间的1号铁轨持续干扰！").getBytes(),
					DataLevel.ContinuousInterference);
			tAnd2R.changeLeftRailContinuousInterference();
		} else {
			((CommandFragment) MainActivity.getMainActivity().commandFragment).AddCmdMsg("收到未定义数据".getBytes(),
					DataLevel.ContinuousInterference);
		}
	}

	private void setRailRightState(int index, int onOff) {
		TerminalAnd2Rails tAnd2R = terminalAnd2Rails.get(index);
		if (onOff == 0) {// 通的
			tAnd2R.changeRightRailNormal();
		} else if (onOff == 7) {// 断的
			int tNo = tAnd2R.terminalNo;
			int tNextNo = terminalAnd2Rails.get(index + 1).terminalNo;

			((CommandFragment) MainActivity.getMainActivity().commandFragment).AddCmdMsg(
					(String.valueOf(tNo) + "号终端与" + String.valueOf(tNextNo) + "号终端之间的2号铁轨断开！").getBytes(),
					DataLevel.Error);
			tAnd2R.changeRightRailError();
		} else if (onOff == 9) {// 超时
			int tNo = tAnd2R.terminalNo;
			int tNextNo = terminalAnd2Rails.get(index + 1).terminalNo;

			((CommandFragment) MainActivity.getMainActivity().commandFragment).AddCmdMsg(
					(String.valueOf(tNo) + "号终端与" + String.valueOf(tNextNo) + "号终端之间的2号铁轨超时！").getBytes(),
					DataLevel.Timeout);
			tAnd2R.changeRightRailTimeout();
		} else if (onOff == 0x0a) {// 持续干扰
			int tNo = tAnd2R.terminalNo;
			int tNextNo = terminalAnd2Rails.get(index + 1).terminalNo;

			((CommandFragment) MainActivity.getMainActivity().commandFragment).AddCmdMsg(
					(String.valueOf(tNo) + "号终端与" + String.valueOf(tNextNo) + "号终端之间的2号铁轨持续干扰！").getBytes(),
					DataLevel.ContinuousInterference);
			tAnd2R.changeRightRailContinuousInterference();
		} else {
			((CommandFragment) MainActivity.getMainActivity().commandFragment).AddCmdMsg("收到未定义数据".getBytes(),
					DataLevel.ContinuousInterference);
		}
	}

	private void setRailLeftDifferent(int index) {
		TerminalAnd2Rails tAnd2R = terminalAnd2Rails.get(index);
		tAnd2R.changeLeftRailDifferent();
	}

	private void setRailRightDifferent(int index) {
		TerminalAnd2Rails tAnd2R = terminalAnd2Rails.get(index);
		tAnd2R.changeRightRailDifferent();
	}

	private void handleBroadcastFileSize(byte[] data) {
		int size = ((data[6] & 0xff) << 16) + ((data[7] & 0xff) << 8) + (data[8] & 0xff);
		long configFileSize;
		try {
			String path = MainActivity.getMainActivity().getFilesDir() + "/config.xml";
			configFileSize = FileOperate.getFileSize(new File(path));
			if (configFileSize == size) {
				AppendMessage("配置文件大小" + String.valueOf(configFileSize) + "字节，与服务器相同，不需下载。", DataLevel.Warning);
			} else {
				new AlertDialog.Builder(MainActivity.getMainActivity()).setTitle("系统提示")// 设置对话框标题
						.setMessage("配置文件发生改变，大小" + String.valueOf(size) + "字节，是否下载？")// 设置显示的内容
						.setPositiveButton("下载", new DialogInterface.OnClickListener() {// 添加确定按钮
							@Override
							public void onClick(DialogInterface dialog, int which) {// 确定按钮的响应事件
								btnDownloadListener.onClick(vTabHome);
							}
						}).setNegativeButton("取消", new DialogInterface.OnClickListener() {// 添加返回按钮
							@Override
							public void onClick(DialogInterface dialog, int which) {// 响应事件

							}
						}).show();// 在按键响应事件中显示此对话框

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void AppendMessage(String msg, DataLevel level) {
		((CommandFragment) MainActivity.getMainActivity().commandFragment).AddCmdMsg(msg.getBytes(), level);
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
