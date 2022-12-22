﻿using LayerBLL.Utils;
using LayerBOL.Models;
using Microsoft.AspNetCore.Mvc;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Numerics;
using System.Text;
using System.Threading.Tasks;
using LayerDAL.Services;

namespace LayerBLL.Logics
{
    /// <summary>
    /// Classe que contém a lógica do response da entidade Loja
    /// </summary>
    public class LojaLogic
    {
        #region DEFAULT REQUESTS

        /// <summary>
        /// Método que recebe os dados do serviço de obter todos os produtos de todas as lojas
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Loja> lojaList = await LojaService.GetAllService(sqlDataSource);

            if (lojaList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de produtos obtidos com sucesso";
                response.Data = new JsonResult(lojaList);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe os dados do serviço de obter um produto em específico
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do produto da Loja que é pretendido retornar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Loja loja = await LojaService.GetByIDService(sqlDataSource, targetID);

            if (loja != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Produto obtido com sucesso!";
                response.Data = new JsonResult(loja);
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de criar um produto
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="newLoja">Objeto com os dados do produto da Loja a ser criado</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PostLogic(string sqlDataSource, Loja newLoja)
        {
            Response response = new Response();
            bool creationResult = await LojaService.PostService(sqlDataSource, newLoja);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Produto criado com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de atualizar um produto
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="loja">Objeto que contém os dados atualizados do produto</param>
        /// <param name="targetID">ID do Produto da Loja que é pretendido atualizar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> PatchLogic(string sqlDataSource, Loja loja, int targetID)
        {
            Response response = new Response();
            bool updateResult = await LojaService.PatchService(sqlDataSource, loja, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Produto alterado com sucesso!");
            }

            return response;
        }

        /// <summary>
        /// Método que recebe a resposta do serviço de eliminar um produto
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do produto da Loja que é pretendido eliminar</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await LojaService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Produto apagado com sucesso!");
            }

            return response;
        }

        #endregion

        #region BACKLOG REQUESTS

        /// <summary>
        /// Método que recebe os dados do serviço de obter os produtos de um ginásio específico
        /// </summary>
        /// <param name="sqlDataSource">String de Conexão à database</param>
        /// <param name="targetID">ID do ginásio que é pretendido retornar os produtos</param>
        /// <returns>Resposta do pedido feito no serviço</returns>
        public static async Task<Response> GetAllByGinasioIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            List<Loja> loja = await LojaService.GetAllByGinasioIDService(sqlDataSource, targetID);

            if (loja.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Produtos obtidos com sucesso!";
                response.Data = new JsonResult(loja);
            }

            return response;
        }

        #endregion
    }
}
