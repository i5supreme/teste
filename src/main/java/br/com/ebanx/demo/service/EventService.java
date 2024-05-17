package br.com.ebanx.demo.service;

import br.com.ebanx.demo.dto.EventDto;
import br.com.ebanx.demo.model.Account;
import br.com.ebanx.demo.model.Event;
import br.com.ebanx.demo.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    @Autowired
    EventRepository eventRepository;

    public Event createEvent(EventDto eventDto) {
        if (eventRepository.getAccounts().isEmpty()) {
            // new account
            return eventRepository.createAccountWithBalance(eventDto);
        } else {
            // update account
            Account account = eventRepository.getAccounts().get(eventDto.getDestination());
            if (account == null) {
                return eventRepository.createAccountWithBalance(eventDto);
            }
            switch (eventDto.getType()) {
                case "withdraw":
                    if (account.getBalance().compareTo(eventDto.getAmount()) < 0) {
                        return null;
                    } else {
                        account.setBalance(
                                account.getBalance().subtract(eventDto.getAmount()));
                    }

                    break;
                case "deposit":
                    account.setBalance(
                            account.getBalance().add(eventDto.getAmount())
                    );
                    break;
                case "transfer":

                    // Verificar se a conta de destino existe
                    Account origin = eventRepository.getAccounts().get(eventDto.getOrigin());

                    // Se a conta de destino existir
                    if (origin != null) {

                        // se tem saldo na conta de origem
                        if (account.getBalance().compareTo(eventDto.getAmount()) >= 0) {
                            // diminuir o valor da conta
                            origin.setBalance(origin.getBalance().subtract(eventDto.getAmount()));

                            // somar o valor pra conta de destino
                            account.setBalance(account.getBalance().add(eventDto.getAmount()));

                            // Atualizar as informações das contas no repositório
                            eventRepository.updateAccountWithBalance(origin, eventDto);
                        }
                    } else {
                        // se a conta de destino não existir
                        return null;

                    }
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + eventDto.getType());
            }

            return eventRepository.updateAccountWithBalance(account, eventDto);
        }

    }

    public void reset() {
        eventRepository.reset();
    }
}