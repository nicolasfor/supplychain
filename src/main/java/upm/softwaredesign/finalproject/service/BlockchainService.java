package upm.softwaredesign.finalproject.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upm.softwaredesign.finalproject.blockchain.BlockChain;
import upm.softwaredesign.finalproject.entity.BlockchainEntity;
import upm.softwaredesign.finalproject.repository.BlockchainRepository;

@Service
public class BlockchainService {

    private final BlockchainRepository repository;

    @Autowired
    public BlockchainService(BlockchainRepository repository) {
        this.repository = repository;
    }


    public BlockchainEntity saveBlockchain(BlockChain blockchain) throws IOException {

    	//TODO: return toJSON method
        //List<BlockchainEntity> blockchains = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        if (!blockchain.toJSON().isEmpty()) {
        	BlockchainEntity blockChainEntity = new BlockchainEntity();
        	blockChainEntity.setContent(blockchain.toJSON());
            //TypeFactory typeFactory = objectMapper.getTypeFactory();
            //CollectionType valueType = typeFactory.constructCollectionType(List.class, BlockchainEntity.class);
            //blockchains = objectMapper.readValue(blockchain.toJSON(), valueType);
            
            //blockchains.add(blockChainEntity);
            //String blockchainContent = objectMapper.writeValueAsString(blockchains);
            //blockchain.setContent(blockchainContent);
            return this.repository.save(blockChainEntity);
        }
        //TODO define return value
        return null;
        
    }

    public BlockChain retrieveBlockchain() {
        List<BlockchainEntity> blockchains = this.repository.findAll();
        if (blockchains.isEmpty()) {
            return new BlockChain(this);
        }
        BlockchainEntity blockchainEntity = blockchains.get(0);
        

        BlockChain blockChain = new BlockChain(this);
        blockChain.fromJSON(blockchainEntity.getContent());

        return blockChain;
    }

}