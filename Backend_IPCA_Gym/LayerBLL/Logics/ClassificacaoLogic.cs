using LayerBLL.Utils;
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
    public class ClassificacaoLogic
    { 
        public static async Task<Response> GetAllLogic(string sqlDataSource)
        {
            Response response = new Response();
            List<Classificacao> classificacaoList = await ClassificacaoService.GetAllService(sqlDataSource);

            if (classificacaoList.Count != 0)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Lista de classificacoes obtida com sucesso";
                response.Data = new JsonResult(classificacaoList);
            }

            return response;
        }

        public static async Task<Response> GetByIDLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            Classificacao classificacao = await ClassificacaoService.GetByIDService(sqlDataSource, targetID);

            if (classificacao != null)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Classificacao obtida com sucesso";
                response.Data = new JsonResult(classificacao);
            }

            return response;
        }

        public static async Task<Response> PostLogic(string sqlDataSource, Classificacao newClassificacao)
        {
            Response response = new Response();
            bool creationResult = await ClassificacaoService.PostService(sqlDataSource, newClassificacao);

            if (creationResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Classificacao adicionada com sucesso");
            }

            return response;
        }

        public static async Task<Response> PatchLogic(string sqlDataSource, Classificacao classificacao, int targetID)
        {
            Response response = new Response();
            bool updateResult = await ClassificacaoService.PatchService(sqlDataSource, classificacao, targetID);

            if (updateResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Classificacao alterada com sucesso");
            }

            return response;
        }

        public static async Task<Response> DeleteLogic(string sqlDataSource, int targetID)
        {
            Response response = new Response();
            bool deleteResult = await ClassificacaoService.DeleteService(sqlDataSource, targetID);

            if (deleteResult)
            {
                response.StatusCode = StatusCodes.SUCCESS;
                response.Message = "Success!";
                response.Data = new JsonResult("Classificacao removida com sucesso");
            }

            return response;
        }
    }
}
