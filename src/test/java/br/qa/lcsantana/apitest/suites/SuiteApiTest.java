package br.qa.lcsantana.apitest.suites;

import br.qa.lcsantana.apitest.LoginTest;
import br.qa.lcsantana.apitest.ProdutosTest;
import br.qa.lcsantana.apitest.UsuariosTest;
import org.junit.platform.suite.api.SelectClasses;

@org.junit.platform.suite.api.Suite
@SelectClasses({
        ProdutosTest.class,
        UsuariosTest.class,
        LoginTest.class,
})
public class SuiteApiTest {

}
