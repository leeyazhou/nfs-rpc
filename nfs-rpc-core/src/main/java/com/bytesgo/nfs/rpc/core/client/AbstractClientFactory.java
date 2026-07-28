package com.bytesgo.nfs.rpc.core.client;

/**
 * nfs-rpc Apache License
 * 
 * http://code.google.com/p/nfs-rpc (c) 2011
 */
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadLocalRandom;

import com.bytesgo.nfs.rpc.core.exception.RpcRejectException;

/**
 * Abstract Client Factory,create custom nums client
 * 
 * @author <a href="mailto:bluedavy@gmail.com">bluedavy</a>
 */
public abstract class AbstractClientFactory implements ClientFactory {

  // Cache client
  private static final ConcurrentHashMap<String, FutureTask<List<Client>>> clients = new ConcurrentHashMap<>();

  private static volatile boolean isSendLimitEnabled = false;

  public Client get(final String targetIP, final int targetPort, final int connectTimeout, String... customKey) throws Exception {
    return get(targetIP, targetPort, connectTimeout, 1, customKey);
  }

  public Client get(final String targetIP, final int targetPort, final int connectTimeout, final int clientNums, String... customKey)
      throws Exception {
    String key = targetIP + ":" + targetPort;
    if (customKey != null && customKey.length == 1) {
      key = customKey[0];
    }
    FutureTask<List<Client>> task = clients.get(key);
    if (task != null) {
      List<Client> clientList = task.get();
      if (clientNums == 1) {
        return clientList.get(0);
      }
      return clientList.get(ThreadLocalRandom.current().nextInt(clientNums));
    }
    final String cacheKey = key;
    FutureTask<List<Client>> newTask = new FutureTask<>(new Callable<List<Client>>() {
      public List<Client> call() throws Exception {
        List<Client> clientList = new CopyOnWriteArrayList<>();
        for (int i = 0; i < clientNums; i++) {
          clientList.add(createClient(targetIP, targetPort, connectTimeout, cacheKey));
        }
        return clientList;
      }
    });
    FutureTask<List<Client>> existing = clients.putIfAbsent(key, newTask);
    if (existing == null) {
      newTask.run();
      task = newTask;
    } else {
      task = existing;
    }
    List<Client> clientList = task.get();
    if (clientNums == 1) {
      return clientList.get(0);
    }
    return clientList.get(ThreadLocalRandom.current().nextInt(clientNums));
  }

  public void removeClient(String key, Client client) {
    FutureTask<List<Client>> task = clients.get(key);
    if (task == null) {
      return;
    }
    try {
      List<Client> clientList = task.get();
      clientList.remove(client);
      if (clientList.isEmpty()) {
        clients.remove(key);
      }
    } catch (Exception e) {
      // IGNORE
    }
  }

  public void enableSendLimit() {
    isSendLimitEnabled = true;
  }

  /**
   * check if sending bytes size exceed limit threshold
   */
  public void checkSendLimit() throws Exception {
    if (!isSendLimitEnabled)
      return;
    long threshold = javaHeapSize * sendLimitPercent / 100;
    long sendingBytesSize = getSendingBytesSize();
    if (sendingBytesSize >= threshold) {
      if (sendLimitPolicy == SendLimitPolicy.REJECT) {
        throw new RpcRejectException(sendingBytesSize, threshold);
      } else {
        Thread.sleep(1000);
        sendingBytesSize = getSendingBytesSize();
        if (sendingBytesSize >= threshold) {
          throw new RpcRejectException(sendingBytesSize, threshold);
        }
      }
    }
  }

  private long getSendingBytesSize() throws Exception {
    long sendingBytesSize = 0;
    for (FutureTask<List<Client>> clientListTask : clients.values()) {
      List<Client> clientList = clientListTask.get();
      for (Client client : clientList) {
        sendingBytesSize += client.getSendingBytesSize();
      }
    }
    return sendingBytesSize;
  }

  public static ClientFactory getInstance() {
    throw new UnsupportedOperationException("should be implemented by true class");
  }

  protected abstract Client createClient(String targetIP, int targetPort, int connectTimeout, String key) throws Exception;

}
