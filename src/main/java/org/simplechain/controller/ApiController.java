package org.simplechain.controller;

import org.simplechain.blockchain.Blockchain;
import org.simplechain.result.Result;
import org.simplechain.service.BlockChainService;
import org.simplechain.vo.TransactionParam;
import org.simplechain.vo.WalletVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/api")
public class ApiController {
    @Resource
    BlockChainService blockChainService;

    @GetMapping("/wallet/new")
    @ResponseBody
    public Result<WalletVo> newWallet() {
        return Result.success("Generate a new wallet.", blockChainService.generateNewWallet());
    }

    @GetMapping("/miner/{publicKey}/start")
    @ResponseBody
    public Result<Boolean> minerStart(@PathVariable String publicKey) {

        if (blockChainService.minerStatus) {
            return Result.error("Miner Already Started.");
        }

        blockChainService.minerStart(publicKey);
        return Result.success("Miner Started", true);
    }

    @PostMapping("/miner/stop")
    @ResponseBody
    public Result<Boolean> minerStop() {
        blockChainService.minerStop();
        return Result.success("Miner Stoped", true);
    }

    @PostMapping("/transaction/new")
    @ResponseBody
    public Result<Boolean> newTransaction(@RequestBody TransactionParam param) {
        blockChainService.newTransaction(param);
        return Result.success("New transaction success", true);
    }

    @GetMapping("/fullBlockChain")
    @ResponseBody
    public Result<Blockchain> fullBlockChain() {
        return Result.success("Get fullBlockChain info", blockChainService.getFullBlockChain());
    }

    @GetMapping("/balance/{publicKey}")
    @ResponseBody
    public Result<Double> getBalance(@PathVariable String publicKey) {
        return Result.success("Wallet:" + publicKey + " balance", blockChainService.getBalance(publicKey));
    }
}