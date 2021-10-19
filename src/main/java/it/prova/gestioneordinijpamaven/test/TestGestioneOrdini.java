package it.prova.gestioneordinijpamaven.test;

import java.util.Date;
import java.util.List;

import it.prova.gestioneordinijpamaven.dao.EntityManagerUtil;
import it.prova.gestioneordinijpamaven.model.Articolo;
import it.prova.gestioneordinijpamaven.model.Categoria;
import it.prova.gestioneordinijpamaven.model.Ordine;
import it.prova.gestioneordinijpamaven.service.MyServiceFactory;
import it.prova.gestioneordinijpamaven.service.articolo.ArticoloService;
import it.prova.gestioneordinijpamaven.service.categoria.CategoriaService;
import it.prova.gestioneordinijpamaven.service.ordine.OrdineService;

public class TestGestioneOrdini {

	public static void main(String[] args) {

		ArticoloService articoloServiceInstance = MyServiceFactory.getArticoloServiceInstance();
		CategoriaService categoriaServiceInstance = MyServiceFactory.getCategoriaServiceInstance();
		OrdineService ordineServiceInstance = MyServiceFactory.getOrdineServiceInstance();

		try {

			System.out
					.println("In tabella Articolo ci sono " + articoloServiceInstance.listAll().size() + " elementi.");
			System.out.println(
					"In tabella Categoria ci sono " + categoriaServiceInstance.listAll().size() + " elementi.");
			System.out.println("In tabella Ordine ci sono " + ordineServiceInstance.listAll().size() + " elementi.");

			System.out.println(
					"**************************** inizio batteria di test ********************************************");
			System.out.println(
					"*************************************************************************************************");

			testInserisciNuovoOrdine(ordineServiceInstance);

			testInserimentoCategoria(categoriaServiceInstance);

			testInserisciArticoliCollegoAOrdine(ordineServiceInstance, articoloServiceInstance);

			testInserimentoOrdineEArticolo(ordineServiceInstance, articoloServiceInstance, categoriaServiceInstance);

			testAggiornaArticolo(articoloServiceInstance);

			testAggiornaCategoria(categoriaServiceInstance);

			testRimozioneArticoloECheckCategoria(articoloServiceInstance, categoriaServiceInstance);

			testDammiOrdiniCategoria(articoloServiceInstance, categoriaServiceInstance, ordineServiceInstance);

			testDammiCategorieByArticoloOrdine(articoloServiceInstance, categoriaServiceInstance,
					ordineServiceInstance);

			testCalcolaSommaTotaleArticoloDiCategoria(articoloServiceInstance, categoriaServiceInstance);

			System.out.println(
					"****************************** fine batteria di test ********************************************");
			System.out.println(
					"*************************************************************************************************");
			System.out
					.println("In tabella Articolo ci sono " + articoloServiceInstance.listAll().size() + " elementi.");
			System.out.println(
					"In tabella Categoria ci sono " + categoriaServiceInstance.listAll().size() + " elementi.");
			System.out.println("In tabella Ordine ci sono " + ordineServiceInstance.listAll().size() + " elementi.");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			EntityManagerUtil.shutdown();
		}

	}

	// CREA NUOVO ORDINE

	private static void testInserisciNuovoOrdine(OrdineService ordineServiceInstance) throws Exception {
		System.out.println(".......testInserisciNuovoOrdine inizio.............");

		Ordine ordineInstance = new Ordine("vincenzo", "via marsala 21");
		ordineServiceInstance.inserisciNuovo(ordineInstance);
		if (ordineInstance.getId() == null)
			throw new RuntimeException("testInserisciNuovoOrdine fallito: impossibile inserire ordine! ");

		System.out.println(".......testInserisciNuovoOrdine fine: TUTTO OKAY!.............");
	}

	private static void testInserimentoCategoria(CategoriaService categoriaService) throws Exception {

		System.out.println("---------------testInserimentoCategoria inizio .................................");
		Categoria nuovacategoria = new Categoria("Smartphone");

		categoriaService.inserisciNuovo(nuovacategoria);

		if (nuovacategoria.getId() == null)
			throw new RuntimeException("testInserimentoCategoria fallito: la categoria non è stata inserita");

		System.out.println("------------------testInserimentoArticolo PASSED: TUTTO OKAY!------------");
	}

	// CREA NUOVO ARTICOLO COLLEGATO AD ORDINE

	private static void testInserisciArticoliCollegoAOrdine(OrdineService ordineService,
			ArticoloService articoloService) throws Exception {
		System.out.println("----------testInserisciArticoliCollegoAOrdine inizio--------");

		List<Ordine> listaOrdiniPresenti = ordineService.listAll();
		if (listaOrdiniPresenti.isEmpty())
			throw new RuntimeException(
					"testInserisciArticoliCollegoAOrdine fallito: non ci sono ordini a cui collegarci ");

		Articolo nuovoArticolo = new Articolo("Playstation 5", 499);
		nuovoArticolo.setOrdine(listaOrdiniPresenti.get(0));
		articoloService.inserisciNuovo(nuovoArticolo);
		if (nuovoArticolo.getId() == null)
			throw new RuntimeException("testInserisciArticoliCollegoAOrdine fallito ");
		if (nuovoArticolo.getOrdine() == null)
			throw new RuntimeException("testInserisciArticoliCollegoAOrdine fallito: impossibile collegare ordine! ");

		System.out.println(".......testInserisciArticoliCollegoAOrdine fine: TUTTO OKAY!.............");
	}

	// CREA COLLEGAMENTO CATEGORIA ARTICOLO ORDINE

	private static void testInserimentoOrdineEArticolo(OrdineService ordineService, ArticoloService articoloService,
			CategoriaService categoriaService) throws Exception {

		System.out.println("-----------------testInserimentoOrdineEArticolo inizio ---------");

		Ordine nuovoOrdine = new Ordine("Via Trapani 10", "Alberto");
		ordineService.inserisciNuovo(nuovoOrdine);

		if (nuovoOrdine.getId() == null)
			throw new RuntimeException("testInserimentoOrdineEArticolo fallito: ordine non inserito");

		Articolo nuovoArticolo = new Articolo("Samsung Galaxy", 850);
		nuovoArticolo.setOrdine(nuovoOrdine);
		articoloService.inserisciNuovo(nuovoArticolo);

		articoloService.aggiungiCategoria(nuovoArticolo, categoriaService.listAll().get(0));

		if (nuovoArticolo.getId() == null)
			throw new RuntimeException("testInserimentoOrdineEArticolo fallito: articolo non  inserito");

		System.out.println(
				"---------------testInserimentoOrdineEArticolo PASSATO: TUTTO OKAY! ................................");

	}

	// UPDATE ARTICOLO

	private static void testAggiornaArticolo(ArticoloService articoloServiceInstance) throws Exception {

		System.out.println("--------testAggiornaArticolo inizio.-----------");

		Articolo articoloInstance = new Articolo("articolo001", 150);
		articoloServiceInstance.inserisciNuovo(articoloInstance);

		String descrizioneDaAggiornare = "Articolo002";
		articoloInstance.setDescrizione(descrizioneDaAggiornare);
		articoloServiceInstance.aggiorna(articoloInstance);

		if (!articoloServiceInstance.caricaSingoloElemento(articoloInstance.getId()).getDescrizione()
				.equals(descrizioneDaAggiornare))
			throw new RuntimeException("testAggiornaArticolo fallito: impossibile aggiornare articolo ");

		articoloServiceInstance.rimuovi(articoloInstance);
		if (articoloServiceInstance.caricaSingoloElemento(articoloInstance.getId()) != null)
			throw new RuntimeException("testAggiornaArticolo fallito: eliminazione articolo fallita ");

		System.out.println("-------------testAggiornaArticolo fine: TUTTO OKAY!------------");
	}

	// TEST RIMUOVI ARTICOLO E FAI IL CHECK DELLA CATEGORIA

	private static void testRimozioneArticoloECheckCategoria(ArticoloService articoloServiceInstance,
			CategoriaService categoriaServiceInstance) throws Exception {

		System.out.println("----------testRimozioneArticoloECheckCategoria inizio--------");

		long nowInMillisecondi = new Date().getTime();
		Articolo articoloInstanceX = new Articolo("Samsung Galaxy" + nowInMillisecondi, 600);
		articoloServiceInstance.inserisciNuovo(articoloInstanceX);

		Categoria nuovaCategoria1 = new Categoria("Smartphone" + nowInMillisecondi);
		categoriaServiceInstance.inserisciNuovo(nuovaCategoria1);
		Categoria nuovaCategoria2 = new Categoria("Elettronica" + nowInMillisecondi + 1);

		categoriaServiceInstance.inserisciNuovo(nuovaCategoria2);
		articoloServiceInstance.aggiungiCategoria(articoloInstanceX, nuovaCategoria1);
		articoloServiceInstance.aggiungiCategoria(articoloInstanceX, nuovaCategoria2);

		Articolo articoloReloaded = articoloServiceInstance
				.caricaSingoloElementoEagerCategoria(articoloInstanceX.getId());
		if (articoloReloaded.getCategorie().size() != 2)
			throw new RuntimeException(
					"testRimozioneArticoloECheckCategoria fallito: categoria e articoli non collegati ");

		articoloServiceInstance.rimuovi(articoloReloaded);

		Articolo articoloSupposedToBeRemoved = articoloServiceInstance
				.caricaSingoloElementoEagerCategoria(articoloInstanceX.getId());
		if (articoloSupposedToBeRemoved != null)
			throw new RuntimeException("testRimozioneArticoloECheckCategoria fallito: rimozione non avvenuta ");

		System.out.println("---------testRimozioneArticoloECheckCategoria fine: TUTTO OKAY!---------");
	}

	// UPDATE CATEGORIA

	private static void testAggiornaCategoria(CategoriaService categoriaServiceInstance) throws Exception {

		System.out.println(".......testAggiornaCategoria inizio.............");

		Categoria categoriaInstance = new Categoria("Videogiochi");
		categoriaServiceInstance.inserisciNuovo(categoriaInstance);

		String descrizioneDaAggiornare = "Videogiochi Simulator";
		categoriaInstance.setDescrizione(descrizioneDaAggiornare);
		categoriaServiceInstance.aggiorna(categoriaInstance);

		if (!categoriaServiceInstance.caricaSingoloElemento(categoriaInstance.getId()).getDescrizione()
				.equals(descrizioneDaAggiornare))
			throw new RuntimeException("testAggiornaCategoria fallito: categoria non aggiornata ");

		categoriaServiceInstance.rimuovi(categoriaInstance);
		if (categoriaServiceInstance.caricaSingoloElemento(categoriaInstance.getId()) != null)
			throw new RuntimeException("testAggiornaCategoria fallito: eliminazione categoria non riuscita ");

		System.out.println("-----------testAggiornaCategoria fine: TUTTO OKAY!----------");
	}

	// ORDINI EFFETTUATI PER UNA DETERMINATA CATEGORIA

	private static void testDammiOrdiniCategoria(ArticoloService articoloServiceInstance,
			CategoriaService categoriaServiceInstance, OrdineService ordineServiceInstance) throws Exception {

		System.out.println(".......testDammiOrdiniCategoria inizio.............");

		long nowInMillisecondi = new Date().getTime();

		Articolo articoloInstanceX = new Articolo("Xbox One" + nowInMillisecondi, 399);
		articoloServiceInstance.inserisciNuovo(articoloInstanceX);

		Categoria nuovaCategoria = new Categoria("Console" + nowInMillisecondi);
		categoriaServiceInstance.inserisciNuovo(nuovaCategoria);
		articoloServiceInstance.aggiungiCategoria(articoloInstanceX, nuovaCategoria);

		Ordine ordineInstance = new Ordine("Mario", "Trastevere");
		ordineServiceInstance.inserisciNuovo(ordineInstance);
		ordineServiceInstance.aggiungiArticolo(ordineInstance, articoloInstanceX);

		if (ordineServiceInstance.caricaOrdiniFattiPerCategoria(nuovaCategoria).size() == 0)
			throw new RuntimeException("testDammiOrdiniCategoria fallito: ricerca non completa! ");

		System.out.println("---------testDammiOrdiniCategoria fine: TUTTO OKAY!------");
	}

	// CATEGORIE DEGLI ARTICOLI DI UN DETERMINATO ORDINE

	private static void testDammiCategorieByArticoloOrdine(ArticoloService articoloServiceInstance,
			CategoriaService categoriaServiceInstance, OrdineService ordineServiceInstance) throws Exception {

		System.out.println("-----------testDammiCategorieByArticoloOrdine inizio----------");

		long nowInMillisecondi = new Date().getTime();
		Articolo articoloInstanceX = new Articolo("Film Harry Potter" + nowInMillisecondi, 500);
		articoloServiceInstance.inserisciNuovo(articoloInstanceX);

		Categoria categoria1 = new Categoria("Dvd & BlueRay" + nowInMillisecondi);
		categoriaServiceInstance.inserisciNuovo(categoria1);
		articoloServiceInstance.aggiungiCategoria(articoloInstanceX, categoria1);

		Ordine ordineInstance = new Ordine("Giacomo", "Via mosca, 52");
		ordineServiceInstance.inserisciNuovo(ordineInstance);
		ordineServiceInstance.aggiungiArticolo(ordineInstance, articoloInstanceX);

		System.out.println(categoriaServiceInstance.cercaTuttiArticoliOrdini(ordineInstance).size());
		if (categoriaServiceInstance.cercaTuttiArticoliOrdini(ordineInstance).size() == 0)
			throw new RuntimeException(
					"testDammiCategorieByArticoloOrdine fallito: ricerca non avvenuta con successo ");

		System.out.println("---------testDammiCategorieByArticoloOrdine fine: TUTTO OKAY!--------");
	}

	// SOMMA DI TUTTI I PREZZI DEGLI ARTICOLI LEGATI AD UNA DETERMINATA CATEGORIA

	private static void testCalcolaSommaTotaleArticoloDiCategoria(ArticoloService articoloServiceInstance,
			CategoriaService categoriaServiceInstance) throws Exception {

		System.out.println("--------testCalcolaSommaTotaleArticoloDiCategoria inizio--------");

		long nowInMillisecondi = new Date().getTime();
		Articolo articoloInstanceX = new Articolo("Kayak" + nowInMillisecondi, 500);
		articoloServiceInstance.inserisciNuovo(articoloInstanceX);

		Categoria nuovaCategoria = new Categoria("Canoe" + nowInMillisecondi);
		categoriaServiceInstance.inserisciNuovo(nuovaCategoria);
		articoloServiceInstance.aggiungiCategoria(articoloInstanceX, nuovaCategoria);

		System.out.println(articoloServiceInstance.sommaTotalePrezziByCategoria(nuovaCategoria));
		if (articoloServiceInstance.sommaTotalePrezziByCategoria(nuovaCategoria) == null)
			throw new RuntimeException(
					"testCalcolaSommaArticoloDiCategoria fallito: ricerca non avvenuta con successo ");

		System.out.println("---------------testCalcolaSommaByArticoloDiCategoria fine: TUTTO OKAY!----------");
	}

}
