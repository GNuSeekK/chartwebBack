package stock.chart.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import stock.chart.redis.repository.CacheCompleteRepository;

@Service
@RequiredArgsConstructor
public class CacheCompleteService {

    private final CacheCompleteRepository cacheCompleteRepository;

    public boolean isCached(String key) {
        return cacheCompleteRepository.isCached(key);
    }

    public void deleteComplete(String key) {
        cacheCompleteRepository.deleteComplete(key);
    }
}
