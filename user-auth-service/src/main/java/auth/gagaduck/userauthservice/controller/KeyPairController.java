package auth.gagaduck.userauthservice.controller;

import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import com.nimbusds.jose.jwk.RSAKey;

import javax.annotation.Resource;
import java.util.Map;

/*
*
* rsa/publicKey
*
* */
@RestController
@RequestMapping("/rsa")
public class KeyPairController {

    @Resource
    private KeyPair keyPair;

    @GetMapping("/publicKey")
    public Map<String, Object> getKey() {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAKey key = new RSAKey.Builder(publicKey).build();
        return new JWKSet(key).toJSONObject();
    }

}
