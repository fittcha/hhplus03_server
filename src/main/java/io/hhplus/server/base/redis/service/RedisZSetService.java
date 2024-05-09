package io.hhplus.server.base.redis.service;

import io.hhplus.server.base.config.SpringConfig;
import io.hhplus.server.base.redis.RedisConstants;
import io.hhplus.server.base.redis.dto.RedisZSetReqDto;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisZSetService {

    private final StringRedisTemplate stringRedisTemplate;

    private final SpringConfig springConfig;
    private ZSetOperations<String, String> zSetOperations;

    @PostConstruct
    private void init() {
        zSetOperations = stringRedisTemplate.opsForZSet();
    }

    public Boolean zSetAdd(RedisZSetReqDto.ZAdd reqDto) {
        return zSetOperations.add(this.getRedisNameSpace() + reqDto.getKey(), reqDto.getMember(), reqDto.getScore());
    }

    public Long zSetRank(RedisZSetReqDto.ZRank reqDto) {
        return zSetOperations.rank(this.getRedisNameSpace() + reqDto.getKey(), reqDto.getMember());
    }

    public Long zSetRemoveRangeByScore(RedisZSetReqDto.ZRemoveRangeByScore reqDto) {
        Range<Double> range = Range.closed(reqDto.getMin(), reqDto.getMax());
        return zSetOperations.removeRangeByScore(this.getRedisNameSpace() + reqDto.getKey(), range.getLowerBound().getValue().get(), range.getUpperBound().getValue().get());
    }

    public Long zSetSize(RedisZSetReqDto.ZCard reqDto) {
        return zSetOperations.size(this.getRedisNameSpace() + reqDto.getKey());
    }

    public String getRedisNameSpace() {
        return springConfig.getActiveProfile() + ":" + RedisConstants.REDIS_NAME_SPACE_WAITING + ":";
    }
}
