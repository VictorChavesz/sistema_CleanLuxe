// Interface State
interface State {
    void handle();
}

// Contexto
class Pedido {
    private State state;

    public void setState(State state) {
        this.state = state;
    }

    public void request() {
        state.handle();
    }
}

// Estados Concretos
class AguardandoPagamento implements State {
    @Override
    public void handle() {
        System.out.println("Aguardando pagamento...");
    }
}

class Pago implements State {
    @Override
    public void handle() {
        System.out.println("Pedido pago.");
    }
}

// Uso do padrão
public class aplicacao  {
    public static void main(String[] args) {
        Pedido pedido = new Pedido();
        
        pedido.setState(new AguardandoPagamento());
        pedido.request();  // Saída: Aguardando pagamento...

        pedido.setState(new Pago());
        pedido.request();  // Saída: Pedido pago.
    }
}

