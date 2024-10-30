package me.croco.onulmohaji.config;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JasyptConfigTest {

    @Autowired
    @Qualifier("jasyptStringEncryptor")
    private StringEncryptor encryptor;

    @Test
    @DisplayName("jasypt 암/복호화 테스트")
    public void encryptTest() {
        // given
        String value = "암호화할 텍스트";

        // when
        String encrypted = encryptor.encrypt(value);
        String decrypted = encryptor.decrypt(encrypted);

        // then
        System.out.println("encrypted : " + encrypted + " / decrypted : " + decrypted);
        assertThat(decrypted).isEqualTo(value);
    }
}