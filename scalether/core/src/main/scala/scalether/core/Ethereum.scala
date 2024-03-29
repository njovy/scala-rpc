package scalether.core

import java.math.BigInteger

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.rarible.cats.MonadThrowable
import com.rarible.rpc.{JsonConverter, RpcClient, RpcTransport}
import com.rarible.rpc.domain.{Binary, Word}
import scalether.core.json.EthereumJacksonModule
import scalether.domain.request.{LogFilter, Transaction}
import scalether.domain.response.{Block, Log, TransactionReceipt}
import scalether.domain.{Address, response}

import scala.language.higherKinds

class Ethereum[F[_]](transport: RpcTransport[F])
                    (implicit me: MonadThrowable[F])
  extends RpcClient[F](transport) {

  def web3ClientVersion(): F[String] =
    exec("web3_clientVersion")

  def web3Sha3(data: String): F[String] =
    exec("web3_sha3", data)

  def netVersion(): F[String] =
    exec("net_version")

  def netListening(): F[Boolean] =
    exec("net_listening")

  def ethBlockNumber(): F[BigInteger] =
    exec("eth_blockNumber")

  def ethGetBlockByHash(hash: Word): F[Block[Word]] =
    exec("eth_getBlockByHash", hash, false)

  def ethGetFullBlockByHash(hash: Word): F[Block[response.Transaction]] =
    exec("eth_getBlockByHash", hash, true)

  def ethGetBlockByNumber(number: BigInteger): F[Block[Word]] =
    exec("eth_getBlockByNumber", number, false)

  def ethGetFullBlockByNumber(number: BigInteger): F[Block[response.Transaction]] =
    exec("eth_getBlockByNumber", number, true)

  def ethCall(transaction: Transaction, defaultBlockParameter: String): F[Binary] =
    exec("eth_call", transaction, defaultBlockParameter)

  def ethEstimateGas(transaction: Transaction, defaultBlockParameter: String): F[BigInteger] =
    exec("eth_estimateGas", transaction, defaultBlockParameter)

  def ethSendTransaction(transaction: Transaction): F[Word] =
    exec("eth_sendTransaction", transaction)

  def ethSendRawTransaction(transaction: Binary): F[Word] =
    exec("eth_sendRawTransaction", transaction)

  def ethGetTransactionReceipt(hash: Word): F[Option[TransactionReceipt]] =
    execOption("eth_getTransactionReceipt", hash)

  def ethGetTransactionByHash(hash: Word): F[Option[response.Transaction]] =
    execOption("eth_getTransactionByHash", hash)

  def netPeerCount(): F[BigInteger] =
    exec("net_peerCount")

  def ethGetTransactionCount(address: Address, defaultBlockParameter: String): F[BigInteger] =
    exec("eth_getTransactionCount", address, defaultBlockParameter)

  def ethGetBalance(address: Address, defaultBlockParameter: String): F[BigInteger] =
    exec("eth_getBalance", address, defaultBlockParameter)

  def ethGasPrice(): F[BigInteger] =
    exec("eth_gasPrice")

  def ethGetLogs(filter: LogFilter): F[List[Log]] =
    exec("eth_getLogs", filter)

  def ethNewFilter(filter: LogFilter): F[BigInteger] =
    exec("eth_newFilter", filter)

  def ethGetFilterChanges(id: BigInteger): F[List[Log]] =
    exec("eth_getFilterChanges", id)

  def ethGetCode(address: Address, defaultBlockParameter: String): F[Binary] =
    exec("eth_getCode", address, defaultBlockParameter)

  def ethGetStorageAt(address: Address, position: BigInteger, defaultBlockParameter: String): F[Binary] =
    exec("eth_getStorageAt", address, position, defaultBlockParameter)
}

object Ethereum {
  val mapper: ObjectMapper with ScalaObjectMapper = JsonConverter.createMapper(new EthereumJacksonModule)
}
