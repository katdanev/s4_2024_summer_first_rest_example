package com.keyin.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class GreetingService {
    @Autowired
    private GreetingRepository greetingRepository;

    @Autowired
    private LanguageRepository languageRepository;

    public Greeting getGreeting(long index) {
        Optional<Greeting> result = greetingRepository.findById(index);

        if (result.isPresent()) {
            return result.get();
        }

        return null;
    }

    public Greeting createGreeting(Greeting newGreeting) {
        if (newGreeting.getLanguages() == null) {
            Language english = languageRepository.findByName("English");

            if (english == null) {
                english = new Language();
                languageRepository.save(english);
            }

            ArrayList<Language> languageArrayList = new ArrayList<Language>();
            languageArrayList.add(english);

            newGreeting.setLanguages(languageArrayList);
        } else {
            for (Language language : newGreeting.getLanguages()) {
                Language langInDB = languageRepository.findByName(language.getName());

                if (langInDB == null) {
                    language = languageRepository.save(language);
                }
            }
        }

        return greetingRepository.save(newGreeting);
    }

    public List<Greeting> getAllGreetings() {
        return (List<Greeting>) greetingRepository.findAll();
    }


    public Greeting updateGreeting(long index, Greeting updatedGreeting) {
        Greeting updatingGreeting = getGreeting(index);

        // Update basic fields
        updatingGreeting.setName(updatedGreeting.getName());
        updatingGreeting.setGreeting(updatedGreeting.getGreeting());

        // Update languages
        List<Language> updatedLanguagesList = new ArrayList<>();

        for (Language language : updatedGreeting.getLanguages()) {
            Language presentLanguage = languageRepository.findByName(language.getName());
            if (presentLanguage == null) {
                presentLanguage = languageRepository.save(language);
            }
            updatedLanguagesList.add(presentLanguage);
        }
        updatingGreeting.setLanguages(updatedLanguagesList);

        return greetingRepository.save(updatingGreeting);
    }



    public void deleteGreeting(long index) {
        greetingRepository.delete(getGreeting(index));
    }

    public List<Greeting> findGreetingsByNameAndGreeting(String name, String greetingName) {
        return greetingRepository.findByNameAndGreeting(name, greetingName);
    }

}
