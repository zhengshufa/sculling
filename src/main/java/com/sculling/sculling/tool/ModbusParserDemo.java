//package com.sculling.sculling.tool;
//
//public class ModbusParserDemo {
//
//    public static void main(String[] args) {
//        try {
//            // 设置Modbus TCP连接
//            InetAddress address = InetAddress.getByName("127.0.0.1"); // 服务器IP
//            TCPMasterConnection connection = new TCPMasterConnection(address);
//            connection.setPort(502); // 默认Modbus端口
//            connection.connect();
//
//            // 创建进程图像
//            SimpleProcessImage spi = new SimpleProcessImage(100);
//            spi.addProcessImageListener(connection);
//
//            // 读取输入寄存器
//            ReadInputRegisters request = new ReadInputRegisters(0, 10); // 从寄存器0开始读取10个寄存器的数据
//            ModbusTCPTransaction trans = new ModbusTCPTransaction(connection);
//            trans.setRequest(request);
//
//            // 执行事务
//            trans.execute();
//
//            // 处理响应
//            if (trans.getResponse() == null) {
//                throw new IOException("Response was null");
//            }
//
//            if (trans.getResponse() instanceof ReadInputRegistersResponse) {
//                ReadInputRegistersResponse response = (ReadInputRegistersResponse) trans.getResponse();
//                Register[] registers = response.getRegisters();
//                for (int i = 0; i < registers.length; i++) {
//                    Register reg = registers[i];
//                    System.out.println("Register " + i + " : " + reg.toBytes()[0] + " " + reg.toBytes()[1]);
//                }
//            } else {
//                System.out.println("Response was not an ReadInputRegistersResponse");
//            }
//
//            // 关闭连接
//            trans.close();
//            connection.destroy();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
